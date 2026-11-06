# CAT Nusantara

Fondasi awal aplikasi persiapan CAT CPNS independen; belum siap dipakai untuk ujian atau pembayaran. Lihat [status implementasi](docs/IMPLEMENTATION_STATUS.md) sebelum melanjutkan.

## Menjalankan fondasi

Prasyarat: Node.js dan npm. Jalankan `npm ci`, lalu `npm run dev`, dan buka http://localhost:3000. Pemeriksaan: `npm run lint`, `npm run typecheck`, `npm test`, `npm run build`. Docker Compose, database, migrasi dan worker belum tersedia. Jangan gunakan pada produksi.

Lihat [PRD](docs/PRD.md), [arsitektur](docs/ARCHITECTURE.md), [ERD](docs/ERD.md), dan [UI/UX](docs/UI_UX.md) untuk arah implementasi.
