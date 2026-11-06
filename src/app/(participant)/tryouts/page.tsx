import Link from "next/link";
import { redirect } from "next/navigation";
import { auth } from "../../../auth";
import { db } from "../../../lib/db/client";
import { startTryout } from "./actions";

export default async function TryoutsPage() {
  const session = await auth();
  if (!session?.user?.id) redirect("/login");
  const events = await db.tryoutEvent.findMany({ where: { status: "PUBLISHED" }, orderBy: { createdAt: "desc" } });
  return <main className="product-page"><header className="product-header"><Link href="/dashboard" className="product-brand">CAT <b>NUSANTARA</b></Link><Link href="/dashboard">Dashboard</Link></header><section className="product-hero"><p>TRYOUT SKD</p><h1>Uji kesiapanmu dengan simulasi penuh.</h1><span>Timer, urutan soal, jawaban, dan perhitungan skor berjalan di server.</span></section><section className="event-list">{events.map(event => <article className="event-card" key={event.id}><div><p>{event.isInternalDemo ? "LINGKUNGAN INTERNAL" : "TRYOUT"}</p><h2>{event.title}</h2><span>{event.description}</span></div><div className="event-spec"><span>110 SOAL</span><span>{event.durationMinutes} MENIT</span><span>TWK {event.twkCount} · TIU {event.tiuCount} · TKP {event.tkpCount}</span><form action={startTryout}><input type="hidden" name="eventCode" value={event.code} /><button type="submit">Mulai simulasi →</button></form></div></article>)}</section></main>;
}
