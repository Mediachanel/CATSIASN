import { getAuthenticatedUser } from "../../../../../../lib/auth/mobile";
import { db } from "../../../../../../lib/db/client";

export async function GET(request: Request) {
  try {
    const user = await getAuthenticatedUser(request);
    if (!user) {
      return Response.json(
        { success: false, error: "UNAUTHORIZED", message: "Silakan login terlebih dahulu." },
        { status: 401 }
      );
    }

    const { searchParams } = new URL(request.url);
    const sessionId = searchParams.get("sessionId");
    if (!sessionId) {
      return Response.json(
        { success: false, error: "MISSING_SESSION_ID", message: "Parameter sessionId wajib diisi." },
        { status: 400 }
      );
    }

    const session = await db.examSession.findFirst({
      where: { id: sessionId, userId: user.id },
      include: { event: true },
    });

    if (!session) {
      return Response.json(
        { success: false, error: "SESSION_NOT_FOUND", message: "Sesi ujian tidak ditemukan." },
        { status: 404 }
      );
    }

    const now = new Date();
    if (session.status !== "IN_PROGRESS" || session.deadlineAt <= now) {
      if (session.status === "IN_PROGRESS") {
        await db.examSession.update({ where: { id: session.id }, data: { status: "EXPIRED" } });
      }
      return Response.json(
        { success: false, error: "SESSION_CLOSED", message: "Sesi ujian telah selesai atau waktu habis." },
        { status: 403 }
      );
    }

    const sessionQuestions = await db.sessionQuestion.findMany({
      where: { sessionId },
      orderBy: { position: "asc" },
      include: {
        answer: true,
        questionVersion: {
          include: {
            question: {
              select: {
                category: true,
                topic: true,
              },
            },
          },
        },
      },
    });

    const remainingSeconds = Math.max(0, Math.floor((session.deadlineAt.getTime() - now.getTime()) / 1000));

    const questions = sessionQuestions.map(sq => {
      const optionOrder = (sq.optionOrder as Array<{ label: string; text: string }>) || [];
      return {
        id: sq.id,
        packageId: session.event.code,
        number: sq.position,
        category: sq.questionVersion.question.category,
        subCategory: sq.questionVersion.question.topic || "Umum",
        questionText: sq.questionVersion.prompt,
        options: optionOrder.map(opt => ({
          key: opt.label,
          text: opt.text,
          score: 0, // Security: Do not expose weights during exam
        })),
        correctKey: null, // Security: Hidden during exam
        explanation: "", // Security: Hidden during exam
        fastTips: "",
        selectedKey: sq.answer?.selectedOption ?? null,
        isDoubtful: false,
      };
    });

    return Response.json({
      success: true,
      data: {
        sessionId: session.id,
        deadlineAt: session.deadlineAt.toISOString(),
        remainingSeconds,
        totalQuestions: questions.length,
        questions,
      },
    });
  } catch (error) {
    return Response.json(
      { success: false, error: error instanceof Error ? error.message : "FETCH_QUESTIONS_FAILED" },
      { status: 500 }
    );
  }
}
