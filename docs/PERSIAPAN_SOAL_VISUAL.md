# Persiapan soal visual dan database terpisah

Migrasi `20261108170000_question_visuals` menambahkan referensi dan teks alternatif gambar ke versi soal. Migrasi ini belum dijalankan pada database mana pun. Deploy aplikasi yang memakai skema baru hanya setelah migrasi berhasil diterapkan ke database tujuan dan Prisma Client dibuat ulang. Database existing membutuhkan migrasi ini sebelum menjalankan kode aplikasi terbaru.

Enam soal TIU visual orisinal ada di `data/bank_visual/soal_visual.json`; aset SVG ada di `public/soal-visual`. Script `scripts/import-visual-questions.mjs` tidak mengubah database kecuali dijalankan dengan `--apply`. Setelah DATABASE_URL diatur ke database pengembangan yang benar dan migrasi dijalankan, `node scripts/import-visual-questions.mjs --apply` menambahkan soal visual sebagai DRAFT; kode soal yang telah ada tidak diubah. Jangan arahkan perintah pada database produksi tanpa proses deploy yang terkontrol.

Soal visual baru beserta 9.000 soal revisi belum diverifikasi oleh ahli dan belum layak disebut bank soal final. Soal visual hanya enam soal awal, belum mencakup pilihan bergambar. Importer bank utama masih menunjuk file lama secara default. Review konten, import versi revisi, pengujian end-to-end dan pengujian perangkat tetap diperlukan. Status DRAFT/isDemo dipertahankan dan soal bukan soal ujian resmi BKN.
