# CAT Nusantara

Fondasi awal aplikasi persiapan CAT CPNS independen; belum siap dipakai untuk ujian atau pembayaran. Lihat [status implementasi](docs/IMPLEMENTATION_STATUS.md).

## Setup lokal

Prasyarat: Node.js 24, npm, Docker Desktop. Salin `.env.example` menjadi `.env`, ganti password lokal, buat `AUTH_SECRET` acak dengan perintah pada `.env.example`, dan pastikan `DATABASE_URL` sesuai dengan kredensial PostgreSQL. Contoh ini hanya untuk pengembangan. Port PostgreSQL host adalah 55432 agar tidak mengganggu instalasi yang sudah ada.

```powershell
npm ci
docker compose up -d --wait postgres redis
npm run db:generate
npm run db:migrate
npm run db:seed
npm run dev
```

Buka http://localhost:3000. Seed hanya menambahkan paket contoh yang belum ada dan tidak menimpa pengaturan paket yang sudah ada. Data tersimpan dalam volume Docker; `docker compose down` menghentikan layanan tanpa menghapus volume. Jangan gunakan `down -v` jika ingin mempertahankan data.

Pemeriksaan: `npm run lint`, `npm run typecheck`, `npm test`, `npm run build`, `npm run test:integration` (memerlukan database yang sudah dimigrasi dan di-seed). MinIO berada pada profil `storage`, tetapi image upstream pada konfigurasi saat ini tidak berhasil diunduh dan belum dapat digunakan. Compose belum mencakup app atau worker. Registrasi dan login peserta tersedia untuk pengujian lokal; reset password, MFA, RBAC admin, bank soal, ujian, dan pembayaran belum diimplementasikan. Jangan gunakan pada produksi.

Lihat [PRD](docs/PRD.md), [arsitektur](docs/ARCHITECTURE.md), [ERD](docs/ERD.md), dan [UI/UX](docs/UI_UX.md).

## Tes autentikasi browser

Setelah database dan Redis siap, jalankan `npx playwright install chromium` sekali, lalu `npm run test:e2e`. Pengujian membuat akun unik dan menghapusnya setelah selesai; gunakan hanya database pengembangan. Pada Windows pengujian juga dapat menggunakan Edge yang sudah terpasang dengan mengatur `PLAYWRIGHT_BROWSER_PATH` ke path executable browser.
