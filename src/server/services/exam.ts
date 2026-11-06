import { QuestionCategory, QuestionStatus } from "@prisma/client";
import { db } from "../../lib/db/client";

const labels = ["A", "B", "C", "D", "E"] as const;
type OptionLabel = (typeof labels)[number];

function shuffled<T>(items: T[]) {
  const copy = [...items];
  for (let index = copy.length - 1; index > 0; index -= 1) {
    const swap = Math.floor(Math.random() * (index + 1));
    [copy[index], copy[swap]] = [copy[swap], copy[index]];
  }
  return copy;
}

function asOptionOrder(value: unknown): Array<{ label: OptionLabel; text: string }> {
  if (!Array.isArray(value)) throw new Error("Invalid option snapshot");
  return value as Array<{ label: OptionLabel; text: string }>;
}

export async function createOrResumeSession(userId: string, eventCode: string) {
  const event = await db.tryoutEvent.findFirst({ where: { code: eventCode, status: "PUBLISHED" } });
  if (!event) throw new Error("Event tidak tersedia");
  const now = new Date();
  const current = await db.examSession.findFirst({
    where: { userId, eventId: event.id, status: "IN_PROGRESS", deadlineAt: { gt: now } },
    orderBy: { startedAt: "desc" },
  });
  if (current) return current;

  const required = [
    [QuestionCategory.TWK, event.twkCount],
    [QuestionCategory.TIU, event.tiuCount],
    [QuestionCategory.TKP, event.tkpCount],
  ] as const;
  const questionStatus = event.isInternalDemo ? QuestionStatus.DRAFT : QuestionStatus.PUBLISHED;
  const pools = await Promise.all(required.map(async ([category, count]) => {
    const rows = await db.questionVersion.findMany({
      where: { status: questionStatus, question: { category } },
      include: { question: { select: { category: true } }, options: { orderBy: { label: "asc" } } },
    });
    if (rows.length < count) throw new Error(`Bank soal ${category} belum mencukupi untuk event ini.`);
    return shuffled(rows).slice(0, count);
  }));
  const selected = shuffled(pools.flat());
  return db.$transaction(async tx => {
    const session = await tx.examSession.create({
      data: { userId, eventId: event.id, deadlineAt: new Date(now.getTime() + event.durationMinutes * 60_000) },
    });
    await tx.sessionQuestion.createMany({
      data: selected.map((version, index) => {
        const optionOrder = shuffled(version.options).map(option => ({ label: option.label as OptionLabel, text: option.text }));
        const weights = Object.fromEntries(version.options.map(option => [option.label, option.weight]));
        const correct = version.options.find(option => option.isCorrect)?.label ?? null;
        return {
          sessionId: session.id,
          questionVersionId: version.id,
          position: index + 1,
          optionOrder,
          correctOption: correct,
          optionWeights: version.question.category === "TKP" ? weights : undefined,
        };
      }),
    });
    return session;
  });
}

async function ownedSession(userId: string, sessionId: string) {
  const session = await db.examSession.findFirst({ where: { id: sessionId, userId }, include: { event: true } });
  if (!session) throw new Error("Sesi ujian tidak ditemukan.");
  return session;
}

export async function getSessionOverview(userId: string, sessionId: string) {
  const session = await ownedSession(userId, sessionId);
  const questions = await db.sessionQuestion.findMany({
    where: { sessionId }, orderBy: { position: "asc" }, select: { id: true, position: true, answer: { select: { selectedOption: true } } },
  });
  return { session, questions: questions.map(question => ({ id: question.id, position: question.position, selectedOption: question.answer?.selectedOption ?? null })) };
}

export async function getQuestionForParticipant(userId: string, sessionId: string, position: number) {
  await ownedSession(userId, sessionId);
  const question = await db.sessionQuestion.findFirst({
    where: { sessionId, position },
    include: { answer: true, questionVersion: { include: { question: { select: { category: true, topic: true } } } } },
  });
  if (!question) throw new Error("Soal tidak ditemukan.");
  return {
    position: question.position,
    prompt: question.questionVersion.prompt,
    category: question.questionVersion.question.category,
    topic: question.questionVersion.question.topic,
    options: asOptionOrder(question.optionOrder),
    selectedOption: question.answer?.selectedOption ?? null,
  };
}

export async function saveAnswer(userId: string, sessionId: string, position: number, selectedOption: string) {
  const session = await ownedSession(userId, sessionId);
  if (session.status !== "IN_PROGRESS") throw new Error("Sesi sudah ditutup.");
  if (session.deadlineAt <= new Date()) {
    await db.examSession.update({ where: { id: session.id }, data: { status: "EXPIRED" } });
    throw new Error("Waktu ujian sudah habis.");
  }
  if (!labels.includes(selectedOption as OptionLabel)) throw new Error("Pilihan jawaban tidak valid.");
  const question = await db.sessionQuestion.findFirst({ where: { sessionId, position } });
  if (!question || !asOptionOrder(question.optionOrder).some(option => option.label === selectedOption)) throw new Error("Soal atau pilihan jawaban tidak valid.");
  await db.examAnswer.upsert({ where: { sessionQuestionId: question.id }, create: { sessionQuestionId: question.id, selectedOption }, update: { selectedOption } });
}

export async function submitSession(userId: string, sessionId: string) {
  return db.$transaction(async tx => {
    const session = await tx.examSession.findFirst({ where: { id: sessionId, userId }, include: { result: true } });
    if (!session) throw new Error("Sesi ujian tidak ditemukan.");
    if (session.result) return session.result;
    const questions = await tx.sessionQuestion.findMany({
      where: { sessionId }, include: { answer: true, questionVersion: { include: { question: { select: { category: true } } } } },
    });
    const scores = { TWK: 0, TIU: 0, TKP: 0 };
    for (const question of questions) {
      const selected = question.answer?.selectedOption;
      if (!selected) continue;
      const category = question.questionVersion.question.category;
      if (category === "TKP") {
        const weights = (question.optionWeights ?? {}) as Record<string, number>;
        scores.TKP += Number(weights[selected] ?? 0);
      } else if (question.correctOption === selected) scores[category] += 5;
    }
    await tx.examSession.update({ where: { id: session.id }, data: { status: session.deadlineAt <= new Date() ? "EXPIRED" : "SUBMITTED", submittedAt: new Date() } });
    return tx.examResult.create({ data: { sessionId, twkScore: scores.TWK, tiuScore: scores.TIU, tkpScore: scores.TKP, totalScore: scores.TWK + scores.TIU + scores.TKP } });
  });
}
