# Visual audit tahap kedua

Tanggal: 2026-11-06. Viewport: 1366×768. Browser: Microsoft Edge melalui Playwright. Referensi visual CAT BKN tidak ditemukan sebagai file lokal di repository, sehingga audit menggunakan pola pengalaman CAT yang diminta: header identitas, timer yang menonjol, satu soal per layar, navigasi nomor, legenda status, dan kontrol aksi. Produk tetap diberi branding CAT Nusantara dan tidak mengklaim afiliasi BKN.

## Hasil

- `/login`: screenshot production lulus. Kartu login terpusat, lebar 510px, input dan CTA memiliki hierarki jelas, kontras serta fokus keyboard tersedia. Ikon CTA yang sebelumnya tampil sebagai `?` diperbaiki menjadi panah.
- `/exam`: sebelumnya 404. Shell UI baru sekarang memiliki header 74px, timer kanan dengan ukuran 24px, bar progress 48px, area soal utama yang dominan, sidebar navigasi 330px, status aktif/terjawab/belum, lima opsi dengan target klik besar, dan tombol aksi di bagian bawah. Pada 1366×768 tidak ada overflow horizontal atau tabrakan mayor.
- Warna status: aktif emas, terjawab biru tua, belum putih dengan border; legenda di sidebar memakai status yang sama. Tipografi soal menggunakan sans-serif tebal agar terbaca cepat, sementara metadata dibuat lebih kecil dan berjarak.

## Batasan

`/exam` adalah preview visual dan belum terhubung ke sesi ujian, timer server, autosave, navigasi nyata, atau submit. Tidak ada backend yang diubah pada audit ini. Tombol preview tidak boleh dianggap sebagai mesin ujian aktif.

Screenshot dibuat ulang setelah build production dengan `node scripts/capture-visual-audit.mjs`; hasil lokal berada di `artifacts/visual-audit/login-1366.png` dan `artifacts/visual-audit/exam-1366.png`.
