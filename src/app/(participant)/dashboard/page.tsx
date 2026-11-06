import Link from "next/link";
import { redirect } from "next/navigation";
import { auth } from "../../../auth";
import { db } from "../../../lib/db/client";
import { logout } from "../../(auth)/actions";
export default async function Dashboard() {
  const session = await auth();
  if (!session?.user?.id) redirect("/login");
  const user = await db.user.findUnique({ where: { id: session.user.id }, select: { name: true } });
  if (!user) redirect("/login");
  return <main className="dashboard-shell"><header className="dashboard-header"><Link className="auth-brand" href="/">CAT <b>NUSANTARA</b></Link><form action={logout}><button className="secondary" type="submit">Keluar</button></form></header><section className="dashboard-content"><p className="eyebrow">DASHBOARD PESERTA</p><h1>Halo, {user.name}.</h1><p>Selamat datang di ruang persiapanmu.</p><div className="dashboard-empty"><h2>Persiapan dimulai di sini.</h2><p>Fitur tryout, hasil, dan membership masih dalam pengembangan. Belum ada ujian yang dapat diikuti.</p><Link className="text-link" href="/">Kembali ke beranda ?</Link></div></section></main>
}
