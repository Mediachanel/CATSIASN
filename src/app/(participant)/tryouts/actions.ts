"use server";

import { redirect } from "next/navigation";
import { auth } from "../../../auth";
import { createOrResumeSession } from "../../../server/services/exam";

export async function startTryout(formData: FormData) {
  const session = await auth();
  if (!session?.user?.id) redirect("/login");
  const code = String(formData.get("eventCode") ?? "");
  const exam = await createOrResumeSession(session.user.id, code);
  redirect(`/exam/${exam.id}`);
}
