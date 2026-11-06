import { notFound, redirect } from "next/navigation";
import { auth } from "../../../../auth";
import { ExamRunner } from "../../../../components/exam/exam-runner";
import { getQuestionForParticipant, getSessionOverview } from "../../../../server/services/exam";

export default async function ExamPage({ params }: { params: Promise<{ sessionId: string }> }) {
  const user = await auth();
  if (!user?.user?.id) redirect("/login");
  const { sessionId } = await params;
  let overview;
  let question;
  try {
    overview = await getSessionOverview(user.user.id, sessionId);
    if (overview.session.status !== "IN_PROGRESS") redirect(`/results/${sessionId}`);
    question = await getQuestionForParticipant(user.user.id, sessionId, 1);
  } catch { notFound(); }
  return <ExamRunner sessionId={sessionId} deadlineAt={overview.session.deadlineAt.toISOString()} eventTitle={overview.session.event.title} initialQuestion={question} overview={overview.questions} />;
}
