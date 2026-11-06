import Link from "next/link";
import { notFound, redirect } from "next/navigation";
import { auth } from "../../../../auth";
import { db } from "../../../../lib/db/client";

export default async function ResultPage({ params }: { params: Promise<{ sessionId: string }> }) {
  const user = await auth();
  if (!user?.user?.id) redirect("/login");
  const { sessionId } = await params;
  const session = await db.examSession.findFirst({ where: { id: sessionId, userId: user.user.id }, include: { event: true, result: true, questions: { include: { answer: true } } } });
  if (!session?.result) notFound();
  return <main className="result-page"><header className="product-header"><Link href="/dashboard" className="product-brand">CAT <b>NUSANTARA</b></Link><Link href="/tryouts">Tryout lain</Link></header><section className="result-hero"><p>HASIL SIMULASI</p><h1>{session.event.title}</h1><span>{session.questions.filter(question => question.answer).length} dari {session.questions.length} soal dijawab</span><strong>{session.result.totalScore}<small>/ 550</small></strong></section><section className="score-grid"><article><span>TWK</span><b>{session.result.twkScore}</b><small>maks. 150</small></article><article><span>TIU</span><b>{session.result.tiuScore}</b><small>maks. 175</small></article><article><span>TKP</span><b>{session.result.tkpScore}</b><small>maks. 225</small></article></section><section className="result-note"><h2>Hasil latihan tersimpan.</h2><p>Passing grade dan pembahasan belum diterapkan untuk event internal ini. Soal dalam event masih DRAFT dan perlu review ahli sebelum digunakan untuk evaluasi resmi.</p><Link href="/tryouts">Kembali ke daftar tryout →</Link></section></main>;
}
