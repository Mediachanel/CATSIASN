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

Pemeriksaan: `npm run lint`, `npm run typecheck`, `npm test`, `npm run build`, `npm run test:integration` (memerlukan database yang sudah dimigrasi dan di-seed). MinIO berada pada profil `storage`, tetapi image upstream pada konfigurasi saat ini tidak berhasil diunduh dan belum dapat digunakan. Compose menyediakan app dan worker, tetapi worker saat ini hanya memproses job pemeriksaan, belum ada rekonsiliasi pembayaran atau analitik. Registrasi dan login peserta tersedia untuk pengujian lokal; reset password, MFA, RBAC admin, bank soal, ujian, dan pembayaran belum diimplementasikan. Jangan gunakan pada produksi.

Lihat [PRD](docs/PRD.md), [arsitektur](docs/ARCHITECTURE.md), [ERD](docs/ERD.md), dan [UI/UX](docs/UI_UX.md).

## Tes autentikasi browser

Setelah database dan Redis siap, jalankan `npx playwright install chromium` sekali, lalu `npm run test:e2e`. Pengujian membuat akun unik dan menghapusnya setelah selesai; gunakan hanya database pengembangan. Pada Windows pengujian juga dapat menggunakan Edge yang sudah terpasang dengan mengatur `PLAYWRIGHT_BROWSER_PATH` ke path executable browser.

## Menjalankan app dan worker dalam Compose

Setel `DATABASE_URL_DOCKER` di `.env` agar memakai host `postgres` dan kredensial yang sama dengan layanan PostgreSQL; karakter khusus dalam kredensial URL harus di-encode. Setelah Docker aktif, jalankan `docker compose up -d --build --wait app worker`, kemudian `docker compose exec app npm run db:migrate` dan `docker compose exec app npm run db:seed`. Aplikasi tersedia pada http://localhost:3000. Pengujian worker menggunakan `npm run test:worker` pada host dengan Redis lokal yang sama. Jangan menjalankan pemeriksaan ini pada sistem produksi.

Endpoint `/api/v1/health/live` memeriksa proses HTTP; `/api/v1/health/ready` memeriksa koneksi PostgreSQL dan Redis, bukan kelengkapan migrasi atau kesehatan worker. Batasi endpoint ini pada jaringan internal saat deployment. Worker belum menjalankan job bisnis.

Untuk menjalankan tes browser terhadap app Compose yang sedang aktif, tetapkan `E2E_COMPOSE=1` sebelum `npm run test:e2e`. Pengujian menulis dan menghapus satu akun uji pada database yang ditunjuk `DATABASE_URL`; gunakan database pengembangan yang sama dengan `DATABASE_URL_DOCKER`.
