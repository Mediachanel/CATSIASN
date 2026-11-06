import { auth } from "../../../../../../../auth";
import { submitSession } from "../../../../../../../server/services/exam";

export async function POST(_: Request, { params }: { params: Promise<{ sessionId: string }> }) {
  const session = await auth();
  if (!session?.user?.id) return Response.json({ error: "UNAUTHORIZED" }, { status: 401 });
  try { return Response.json(await submitSession(session.user.id, (await params).sessionId)); }
  catch { return Response.json({ error: "SUBMIT_FAILED" }, { status: 409 }); }
}
