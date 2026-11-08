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
      include: {
        event: true,
        result: true,
      },
    });

    if (!session) {
      return Response.json(
        { success: false, error: "SESSION_NOT_FOUND", message: "Sesi ujian tidak ditemukan." },
        { status: 404 }
      );
    }

    if (session.status === "IN_PROGRESS" && session.deadlineAt > new Date()) {
      return Response.json(
        { success: false, error: "SESSION_ACTIVE", message: "Pembahasan hanya dapat diakses setelah ujian selesai." },
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
            options: true,
          },
        },
      },
    });

    const reviews = sessionQuestions.map(sq => {
      const category = sq.questionVersion.question.category;
      const optionOrder = (sq.optionOrder as Array<{ label: string; text: string }>) || [];
      const weights = (sq.optionWeights as Record<string, number>) || {};
      const selectedKey = sq.answer?.selectedOption ?? null;

      let scoreEarned = 0;
      let isCorrect = false;

      if (selectedKey) {
        if (category === "TKP") {
          scoreEarned = Number(weights[selectedKey] ?? 0);
          isCorrect = scoreEarned === 5;
        } else {
          isCorrect = sq.correctOption === selectedKey;
          scoreEarned = isCorrect ? 5 : 0;
        }
      }

      return {
        id: sq.id,
        number: sq.position,
        category,
        subCategory: sq.questionVersion.question.topic || "Umum",
        questionText: sq.questionVersion.prompt,
        options: optionOrder.map(opt => {
          const optScore = category === "TKP"
            ? Number(weights[opt.label] ?? 0)
            : (opt.label === sq.correctOption ? 5 : 0);
          return {
            key: opt.label,
            text: opt.text,
            score: optScore,
          };
        }),
        correctKey: sq.correctOption,
        explanation: sq.questionVersion.explanation || "Pembahasan belum tersedia untuk soal ini.",
        fastTips: "",
        selectedKey,
        scoreEarned,
        isCorrect,
      };
    });

    return Response.json({
      success: true,
      data: {
        sessionId: session.id,
        eventName: session.event.title,
        result: session.result
          ? {
              twkScore: session.result.twkScore,
              tiuScore: session.result.tiuScore,
              tkpScore: session.result.tkpScore,
              totalScore: session.result.totalScore,
              isPassed:
                session.result.twkScore >= 65 &&
                session.result.tiuScore >= 80 &&
                session.result.tkpScore >= 166,
            }
          : null,
        totalQuestions: reviews.length,
        reviews,
      },
    });
  } catch (error) {
    return Response.json(
      { success: false, error: error instanceof Error ? error.message : "FETCH_REVIEW_FAILED" },
      { status: 500 }
    );
  }
}
