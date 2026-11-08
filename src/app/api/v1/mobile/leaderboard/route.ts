import { db } from "../../../../../lib/db/client";

export async function GET() {
  try {
    const results = await db.examResult.findMany({
      orderBy: { totalScore: "desc" },
      take: 20,
      include: {
        session: {
          include: {
            user: {
              select: {
                id: true,
                name: true,
              },
            },
            event: {
              select: {
                title: true,
              },
            },
          },
        },
      },
    });

    const leaderboard = results.map((r, index) => {
      const twkPassed = r.twkScore >= 65;
      const tiuPassed = r.tiuScore >= 80;
      const tkpPassed = r.tkpScore >= 166;
      return {
        rank: index + 1,
        userId: r.session.user.id,
        userName: r.session.user.name,
        twkScore: r.twkScore,
        tiuScore: r.tiuScore,
        tkpScore: r.tkpScore,
        totalScore: r.totalScore,
        isPassed: twkPassed && tiuPassed && tkpPassed,
        submittedAt: r.createdAt.toISOString(),
      };
    });

    return Response.json({
      success: true,
      data: leaderboard,
    });
  } catch (error) {
    return Response.json(
      { success: false, error: error instanceof Error ? error.message : "FETCH_LEADERBOARD_FAILED" },
      { status: 500 }
    );
  }
}
