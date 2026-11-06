# Bank soal

## Import checkpoint

Tiga file JSON lokal sudah diimpor sebagai `QuestionImportBatch` BATCH01, BATCH02, dan BATCH03. Masing-masing berisi 3.000 baris dengan distribusi TWK 750, TIU 1.050, dan TKP 1.200; total database setelah import adalah 9.000 question, 9.000 question version, dan 45.000 option.

Jalankan `npm run db:import-question-bank` untuk import idempotent. Importer menolak jumlah baris yang salah, kategori tidak dikenal, kode duplikat, kunci TWK/TIU yang tidak valid, dan bobot TKP yang tidak terdiri dari lima nilai unik 1–5. Versi disimpan `DRAFT`, `isDemo=true`, dan terhubung ke batch. Importer menghapus lalu membuat ulang opsi versi agar pengulangan tidak menduplikasi option.

Sumber file diberi label internal/generated dan setiap soal perlu review ahli. Data ini belum terverifikasi, belum dipublikasikan, dan tidak boleh dipresentasikan sebagai soal resmi BKN atau target 3.000 soal tervalidasi. Media belum diimport; kolom media pada JSON belum menjadi `media_assets`.
