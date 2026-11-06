import Link from "next/link";
import { redirect } from "next/navigation";
import { auth } from "../../../auth";
import { db } from "../../../lib/db/client";
import { logout } from "../../(auth)/actions";
export default async function Dashboard() {
  const session = await auth();
  if (!session?.user?.id) redirect("/login");
  const user = await db.user.findUnique({ where: { id: session.user.id }, select: { name: true, examSessions: { orderBy: { startedAt: "desc" }, take: 3, include: { event: true, result: true } } } });
  if (!user) redirect("/login");
  return <main className="product-page"><header className="product-header"><Link className="product-brand" href="/">CAT <b>NUSANTARA</b></Link><nav><Link href="/tryouts">Tryout</Link><form action={logout}><button type="submit">Keluar</button></form></nav></header><section className="dashboard-hero"><p>DASHBOARD PESERTA</p><h1>Halo, {user.name}.</h1><span>Mulai latihan dengan simulasi SKD 110 soal dan lanjutkan sesi yang belum selesai.</span><Link href="/tryouts">Lihat tryout tersedia →</Link></section><section className="dashboard-section"><h2>Riwayat simulasi</h2>{user.examSessions.length ? <div className="history-list">{user.examSessions.map(exam => <article key={exam.id}><div><b>{exam.event.title}</b><span>{exam.status === "IN_PROGRESS" ? "Sedang dikerjakan" : "Selesai"}</span></div>{exam.result ? <strong>{exam.result.totalScore} / 550</strong> : <Link href={`/exam/${exam.id}`}>Lanjutkan →</Link>}</article>)}</div> : <div className="empty-state">Belum ada sesi. Pilih tryout untuk memulai.</div>}</section></main>
}
