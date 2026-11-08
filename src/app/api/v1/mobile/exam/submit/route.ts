import { z } from "zod";
import { getAuthenticatedUser } from "../../../../../../lib/auth/mobile";
import { submitSession } from "../../../../../../server/services/exam";

const submitSchema = z.object({
  sessionId: z.string(),
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

    const json = await request.json().catch(() => null);
    const parsed = submitSchema.safeParse(json);
    if (!parsed.success) {
      return Response.json(
        { success: false, error: "INVALID_BODY", message: "sessionId tidak valid." },
        { status: 400 }
      );
    }

    const result = await submitSession(user.id, parsed.data.sessionId);

    const twkPassed = result.twkScore >= 65;
    const tiuPassed = result.tiuScore >= 80;
    const tkpPassed = result.tkpScore >= 166;
    const isPassed = twkPassed && tiuPassed && tkpPassed;

    return Response.json({
      success: true,
      data: {
        id: result.id,
        sessionId: result.sessionId,
        twkScore: result.twkScore,
        tiuScore: result.tiuScore,
        tkpScore: result.tkpScore,
        totalScore: result.totalScore,
        twkPassed,
        tiuPassed,
        tkpPassed,
        isPassed,
        passingGrades: {
          twk: 65,
          tiu: 80,
          tkp: 166,
          total: 311,
        },
      },
    });
  } catch (error) {
    return Response.json(
      { success: false, error: error instanceof Error ? error.message : "SUBMIT_FAILED" },
      { status: 409 }
    );
  }
}
