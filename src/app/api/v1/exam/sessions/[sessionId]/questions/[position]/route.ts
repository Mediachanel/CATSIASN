import { auth } from "../../../../../../../../auth";
import { getQuestionForParticipant } from "../../../../../../../../server/services/exam";

export const dynamic = "force-dynamic";

export async function GET(_: Request, { params }: { params: Promise<{ sessionId: string; position: string }> }) {
  const session = await auth();
  if (!session?.user?.id) return Response.json({ error: "UNAUTHORIZED" }, { status: 401 });
  const { sessionId, position } = await params;
  const numericPosition = Number(position);
  if (!Number.isInteger(numericPosition) || numericPosition < 1) return Response.json({ error: "INVALID_POSITION" }, { status: 400 });
  try { return Response.json(await getQuestionForParticipant(session.user.id, sessionId, numericPosition)); }
  catch { return Response.json({ error: "NOT_FOUND" }, { status: 404 }); }
}
