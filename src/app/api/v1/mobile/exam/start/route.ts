import { z } from "zod";
import { getAuthenticatedUser } from "../../../../../../lib/auth/mobile";
import { createOrResumeSession } from "../../../../../../server/services/exam";
import { db } from "../../../../../../lib/db/client";

const startSchema = z.object({
  eventCode: z.string().default("DEMO-SKD-110"),
});

export async function POST(request: Request) {
  try {
    const user = await getAuthenticatedUser(request);
    if (!user) {
      return Response.json(
        { success: false, error: "UNAUTHORIZED", message: "Silakan login terlebih dahulu." },
        { status: 401 }
      );
    }

    const json = await request.json().catch(() => ({}));
    const parsed = startSchema.safeParse(json);
    const eventCode = parsed.success ? parsed.data.eventCode : "DEMO-SKD-110";

    const session = await createOrResumeSession(user.id, eventCode);
    const event = await db.tryoutEvent.findUnique({ where: { id: session.eventId } });
    if (!event) {
      return Response.json({ success: false, error: "Event tidak ditemukan." }, { status: 404 });
    }

    const remainingSeconds = Math.max(
      0,
      Math.floor((session.deadlineAt.getTime() - Date.now()) / 1000)
    );

    return Response.json({
      success: true,
      data: {
        sessionId: session.id,
        eventCode: event.code,
        eventTitle: event.title,
        deadlineAt: session.deadlineAt.toISOString(),
        remainingSeconds,
        totalQuestions: event.twkCount + event.tiuCount + event.tkpCount,
        durationMinutes: event.durationMinutes,
        twkCount: event.twkCount,
        tiuCount: event.tiuCount,
        tkpCount: event.tkpCount,
      },
    });
  } catch (error) {
    return Response.json(
      { success: false, error: error instanceof Error ? error.message : "START_EXAM_FAILED" },
      { status: 400 }
    );
  }
}
