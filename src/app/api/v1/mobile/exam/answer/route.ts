import { z } from "zod";
import { getAuthenticatedUser } from "../../../../../../lib/auth/mobile";
import { saveAnswer } from "../../../../../../server/services/exam";

const answerSchema = z.object({
  sessionId: z.string(),
  position: z.number().int().positive(),
  selectedOption: z.enum(["A", "B", "C", "D", "E"]),
  isDoubtful: z.boolean().optional(),
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
    const parsed = answerSchema.safeParse(json);
    if (!parsed.success) {
      return Response.json(
        { success: false, error: "INVALID_BODY", message: "Format jawaban tidak valid." },
        { status: 400 }
      );
    }

    const { sessionId, position, selectedOption } = parsed.data;
    await saveAnswer(user.id, sessionId, position, selectedOption);

    return Response.json({
      success: true,
      status: "saved",
      data: { position, selectedOption },
    });
  } catch (error) {
    return Response.json(
      { success: false, error: error instanceof Error ? error.message : "SAVE_ANSWER_FAILED" },
      { status: 409 }
    );
  }
}
