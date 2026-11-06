import { z } from "zod";
import { auth } from "../../../../../../../auth";
import { saveAnswer } from "../../../../../../../server/services/exam";

const bodySchema = z.object({ position: z.number().int().positive(), selectedOption: z.enum(["A", "B", "C", "D", "E"]) });
export async function PUT(request: Request, { params }: { params: Promise<{ sessionId: string }> }) {
  const session = await auth();
  if (!session?.user?.id) return Response.json({ error: "UNAUTHORIZED" }, { status: 401 });
  const body = bodySchema.safeParse(await request.json().catch(() => null));
  if (!body.success) return Response.json({ error: "INVALID_BODY" }, { status: 400 });
  try {
    await saveAnswer(session.user.id, (await params).sessionId, body.data.position, body.data.selectedOption);
    return Response.json({ status: "saved" });
  } catch (error) { return Response.json({ error: error instanceof Error ? error.message : "SAVE_FAILED" }, { status: 409 }); }
}
