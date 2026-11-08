import { db } from "../../../../../lib/db/client";

export async function GET() {
  try {
    const events = await db.tryoutEvent.findMany({
      where: { status: "PUBLISHED" },
      orderBy: { createdAt: "desc" },
    });

    const packages = events.map(event => {
      const total = event.twkCount + event.tiuCount + event.tkpCount;
      return {
        id: event.code,
        title: event.title,
        badge: event.isInternalDemo ? "INTERNAL DEMO" : "SKD RESMI",
        description: event.description || "Simulasi resmi CAT CPNS dengan standar BKN.",
        type: "FULL_SKD",
        durationMinutes: event.durationMinutes,
        totalQuestions: total,
        twkCount: event.twkCount,
        tiuCount: event.tiuCount,
        tkpCount: event.tkpCount,
        difficulty: "Standar BKN",
        isRecommended: event.code === "DEMO-SKD-110",
        isRandomized: true,
      };
    });

    return Response.json({
      success: true,
      data: packages,
    });
  } catch (error) {
    return Response.json(
      { success: false, error: error instanceof Error ? error.message : "FETCH_FAILED" },
      { status: 500 }
    );
  }
}
