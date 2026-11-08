package com.example.data.seed

import com.example.data.model.ExamCategory
import com.example.data.model.Question
import com.example.data.model.QuestionOption

object TiuQuestionBank {

    val questions: List<Question> = listOf(
        Question(
            id = "tiu_analogi_1",
            packageId = "pool",
            number = 1,
            category = ExamCategory.TIU,
            subCategory = "Analogi Kata",
            questionText = "KENDARAAN : BENSIN = ... : ...",
            options = listOf(
                QuestionOption("A", "Kamera : Foto", 0),
                QuestionOption("B", "Lampu : Listrik", 5),
                QuestionOption("C", "Manusia : Pakaian", 0),
                QuestionOption("D", "Pesawat : Bandara", 0),
                QuestionOption("E", "Kapal : Ombak", 0)
            ),
            correctKey = "B",
            explanation = "Hubungan alat dan sumber energi operasionalnya: Kendaraan membutuhkan Bensin untuk dapat beroperasi/menyala, sama halnya dengan Lampu membutuhkan Listrik untuk menyala.",
            fastTips = "Buat kalimat penghubung: 'A membutuhkan B untuk berfungsi/bekerja'."
        ),
        Question(
            id = "tiu_analogi_2",
            packageId = "pool",
            number = 2,
            category = ExamCategory.TIU,
            subCategory = "Analogi Kata",
            questionText = "APOTEKER : OBAT = KOKI : ...",
            options = listOf(
                QuestionOption("A", "Restoran", 0),
                QuestionOption("B", "Pisau", 0),
                QuestionOption("C", "Masakan", 5),
                QuestionOption("D", "Resep", 0),
                QuestionOption("E", "Dapur", 0)
            ),
            correctKey = "C",
            explanation = "Hubungan profesi dan produk akhir yang diracik/dihasilkan: Apoteker meracik Obat, sedangkan Koki meracik/menghasilkan Masakan.",
            fastTips = "Bedakan antara alat kerja (pisau), tempat kerja (dapur), dan produk akhir (masakan)."
        ),
        Question(
            id = "tiu_silogisme_1",
            packageId = "pool",
            number = 3,
            category = ExamCategory.TIU,
            subCategory = "Silogisme Logis",
            questionText = "Premis 1: Semua atlet yang lolos seleksi nasional memiliki stamina prima.\nPremis 2: Sebagian peserta pelatihan tidak memiliki stamina prima.\nKesimpulan yang tepat adalah...",
            options = listOf(
                QuestionOption("A", "Semua peserta pelatihan bukan atlet yang lolos seleksi nasional", 0),
                QuestionOption("B", "Sebagian peserta pelatihan bukan atlet yang lolos seleksi nasional", 5),
                QuestionOption("C", "Semua peserta pelatihan memiliki stamina prima", 0),
                QuestionOption("D", "Sebagian atlet yang lolos seleksi nasional tidak memiliki stamina prima", 0),
                QuestionOption("E", "Tidak ada atlet yang mengikuti pelatihan", 0)
            ),
            correctKey = "B",
            explanation = "Premis 1 bersifat universal positif (Semua A adalah B). Premis 2 bersifat partikular negatif (Sebagian C bukan B). Kesimpulan yang sah menurut hukum logika formal adalah partikular negatif: Sebagian C bukan A (Sebagian peserta pelatihan bukan atlet yang lolos seleksi nasional).",
            fastTips = "Kaidah Silogisme: Universal + Partikular = Partikular. Positif + Negatif = Negatif. Jadi kesimpulan WAJIB 'Sebagian ... bukan ...'!"
        ),
        Question(
            id = "tiu_silogisme_2",
            packageId = "pool",
            number = 4,
            category = ExamCategory.TIU,
            subCategory = "Silogisme Logis",
            questionText = "Premis 1: Jika hari ini hujan lebat, maka debit air sungai meningkat.\nPremis 2: Jika debit air sungai meningkat, maka pintu air bendungan dibuka.\nFakta: Pintu air bendungan TIDAK dibuka.\nKesimpulan yang sah adalah...",
            options = listOf(
                QuestionOption("A", "Hari ini hujan lebat tetapi tidak banjir", 0),
                QuestionOption("B", "Debit air sungai tetap meningkat", 0),
                QuestionOption("C", "Hari ini tidak hujan lebat", 5),
                QuestionOption("D", "Pintu air bendungan rusak", 0),
                QuestionOption("E", "Hujan lebat terjadi di malam hari", 0)
            ),
            correctKey = "C",
            explanation = "Silogisme Hipotetis + Modus Tollens:\np -> q\nq -> r\nKesimpulan perantara: p -> r (Jika hujan lebat, maka pintu bendungan dibuka).\nFakta: ~r (Pintu air TIDAK dibuka).\nBerdasarkan Modus Tollens: ~p (Hari ini tidak hujan lebat).",
            fastTips = "Pola Tollens: p -> r. Jika tidak r, maka PASTI tidak p!"
        ),
        Question(
            id = "tiu_analitis_1",
            packageId = "pool",
            number = 5,
            category = ExamCategory.TIU,
            subCategory = "Penalaran Analitis",
            questionText = "Lima orang staf (A, B, C, D, E) duduk berderet menghadap panggung seminar:\n1. B duduk tepat di sebelah kanan A.\n2. C duduk di antara B dan D.\n3. E duduk di ujung paling kanan.\nSiapakah yang duduk tepat di tengah barisan?",
            options = listOf(
                QuestionOption("A", "A", 0),
                QuestionOption("B", "B", 0),
                QuestionOption("C", "C", 5),
                QuestionOption("D", "D", 0),
                QuestionOption("E", "E", 0)
            ),
            correctKey = "C",
            explanation = "Analisis urutan kiri ke kanan:\n- B tepat di kanan A: [A, B]\n- C di antara B dan D: [A, B, C, D]\n- E di ujung kanan: [A, B, C, D, E]\nUrutan posisi dari kiri ke kanan: A (ke-1), B (ke-2), C (ke-3), D (ke-4), E (ke-5).\nOrang yang duduk tepat di tengah (posisi ke-3) adalah C.",
            fastTips = "Trik Analitis: Langsung tulis sketsa urutan di kertas buram langkah demi langkah."
        ),
        Question(
            id = "tiu_deret_1",
            packageId = "pool",
            number = 6,
            category = ExamCategory.TIU,
            subCategory = "Deret Angka",
            questionText = "Tentukan suku berikutnya dari deret angka berikut:\n2, 4, 7, 12, 19, 30, ...",
            options = listOf(
                QuestionOption("A", "41", 0),
                QuestionOption("B", "43", 5),
                QuestionOption("C", "45", 0),
                QuestionOption("D", "47", 0),
                QuestionOption("E", "49", 0)
            ),
            correctKey = "B",
            explanation = "Perhatikan pola selisih tingkat pertama:\n4 - 2 = 2\n7 - 4 = 3\n12 - 7 = 5\n19 - 12 = 7\n30 - 19 = 11\nDeret selisihnya adalah bilangan prima: 2, 3, 5, 7, 11, ...\nBilangan prima berikutnya adalah 13.\nMaka suku berikutnya = 30 + 13 = 43.",
            fastTips = "Hafal deret bilangan prima: 2, 3, 5, 7, 11, 13, 17, 19, 23."
        ),
        Question(
            id = "tiu_deret_2",
            packageId = "pool",
            number = 7,
            category = ExamCategory.TIU,
            subCategory = "Deret Angka",
            questionText = "Tentukan dua angka selanjutnya dari deret loncat berikut:\n5, 10, 8, 15, 11, 20, 14, ..., ...",
            options = listOf(
                QuestionOption("A", "25, 17", 5),
                QuestionOption("B", "24, 18", 0),
                QuestionOption("C", "25, 18", 0),
                QuestionOption("D", "22, 16", 0),
                QuestionOption("E", "23, 17", 0)
            ),
            correctKey = "A",
            explanation = "Pola dua deret yang saling loncat (selang-seling):\nDeret Ganjil (posisi 1, 3, 5, 7, 9): 5 (+3)-> 8 (+3)-> 11 (+3)-> 14 (+3)-> 17.\nDeret Genap (posisi 2, 4, 6, 8): 10 (+5)-> 15 (+5)-> 20 (+5)-> 25.\nMaka dua angka selanjutnya adalah posisi ke-8 (25) dan posisi ke-9 (17).",
            fastTips = "Jika pola naik-turun selang-seling, pasti deret loncat 2!"
        ),
        Question(
            id = "tiu_hitung_cepat_1",
            packageId = "pool",
            number = 8,
            category = ExamCategory.TIU,
            subCategory = "Berhitung Cepat",
            questionText = "Jika x = (1/16) - (1/20) dan y = (1/12) - (1/16), maka hubungan yang benar antara x dan y adalah...",
            options = listOf(
                QuestionOption("A", "x > y", 0),
                QuestionOption("B", "x < y", 5),
                QuestionOption("C", "x = y", 0),
                QuestionOption("D", "2x = y", 0),
                QuestionOption("E", "Hubungan x dan y tidak dapat ditentukan", 0)
            ),
            correctKey = "B",
            explanation = "Hitung nilai pecahan:\nx = (1/16) - (1/20) = (5 - 4)/80 = 1/80.\ny = (1/16) di bandingkan (1/12) - (1/16) = (4 - 3)/48 = 1/48.\nKarena penyebut 80 > 48, maka nilai 1/80 < 1/48.\nKesimpulan: x < y.",
            fastTips = "Pecahan dengan pembilang sama (1): semakin besar penyebut, semakin kecil nilainya. 1/80 < 1/48 -> x < y."
        ),
        Question(
            id = "tiu_cerita_kecepatan_1",
            packageId = "pool",
            number = 9,
            category = ExamCategory.TIU,
            subCategory = "Soal Cerita (Kecepatan)",
            questionText = "Kota A dan Kota B berjarak 180 km. Pak Anton berangkat dari Kota A menuju Kota B pada pukul 08.00 dengan kecepatan rata-rata 60 km/jam. Pada saat yang sama, Pak Budi berangkat dari Kota B menuju Kota A dengan kecepatan rata-rata 40 km/jam melalui jalur yang sama. Pada pukul berapa mereka akan berpapasan?",
            options = listOf(
                QuestionOption("A", "09.30", 0),
                QuestionOption("B", "09.48", 5),
                QuestionOption("C", "10.00", 0),
                QuestionOption("D", "10.15", 0),
                QuestionOption("E", "10.30", 0)
            ),
            correctKey = "B",
            explanation = "Rumus Sakti Berpapasan:\nWaktu Berpapasan = Jarak Total ÷ (Kecepatan 1 + Kecepatan 2)\nt = 180 ÷ (60 + 40) = 180 ÷ 100 = 1,8 jam.\nKonversi 0,8 jam ke menit: 0,8 × 60 menit = 48 menit.\nMaka waktu berpapasan = 08.00 + 1 jam 48 menit = Pukul 09.48.",
            fastTips = "Rumus Cepat Berpapasan: t = s / (v1 + v2). 180 / 100 = 1,8 jam = 1 jam 48 menit!"
        ),
        Question(
            id = "tiu_cerita_pekerja_1",
            packageId = "pool",
            number = 10,
            category = ExamCategory.TIU,
            subCategory = "Soal Cerita (Kerja Bersama)",
            questionText = "Andi dapat menyelesaikan penyusunan laporan keuangan dalam waktu 6 jam secara mandiri. Sementara Budi dapat menyelesaikan laporan yang sama dalam waktu 3 jam. Jika Andi dan Budi bekerja sama menyusun laporan tersebut, berapa lama waktu yang mereka butuhkan?",
            options = listOf(
                QuestionOption("A", "1,5 jam", 0),
                QuestionOption("B", "2,0 jam", 5),
                QuestionOption("C", "2,5 jam", 0),
                QuestionOption("D", "3,5 jam", 0),
                QuestionOption("E", "4,5 jam", 0)
            ),
            correctKey = "B",
            explanation = "Rumus Sakti Bekerja Bersama (2 Orang):\nWaktu Bersama = (Waktu A × Waktu B) ÷ (Waktu A + Waktu B)\nt = (6 × 3) ÷ (6 + 3) = 18 ÷ 9 = 2 jam.",
            fastTips = "Rumus Cepat 2 Pekerja: (A x B) / (A + B). (6 x 3)/(6 + 3) = 18/9 = 2 jam tepat!"
        ),
        Question(
            id = "tiu_cerita_untung_1",
            packageId = "pool",
            number = 11,
            category = ExamCategory.TIU,
            subCategory = "Soal Cerita (Persentase)",
            questionText = "Seorang pedagang menjual sebuah laptop seharga Rp4.600.000,00 dan memperoleh keuntungan sebesar 15% dari harga beli. Berapakah harga beli laptop tersebut mula-mula?",
            options = listOf(
                QuestionOption("A", "Rp3.900.000,00", 0),
                QuestionOption("B", "Rp4.000.000,00", 5),
                QuestionOption("C", "Rp4.100.000,00", 0),
                QuestionOption("D", "Rp4.200.000,00", 0),
                QuestionOption("E", "Rp4.350.000,00", 0)
            ),
            correctKey = "B",
            explanation = "Harga Jual = Harga Beli × (100% + %Untung)\nRp4.600.000 = Harga Beli × 115%\nHarga Beli = Rp4.600.000 ÷ 1,15 = Rp4.000.000,00.",
            fastTips = "Harga Beli = Harga Jual / 1.15. 46 / 1.15 = 40 (Rp4.000.000)."
        ),
        Question(
            id = "tiu_figural_1",
            packageId = "pool",
            number = 12,
            category = ExamCategory.TIU,
            subCategory = "Kemampuan Figural",
            questionText = "Sebuah jarum penunjuk pada diagram kompas diputar 90 derajat searah jarum jam, kemudian diputar kembali 135 derajat berlawanan arah jarum jam. Jika posisi awal jarum menunjuk ke arah Timur Laut, kemanakah arah jarum penunjuk sekarang?",
            options = listOf(
                QuestionOption("A", "Utara", 5),
                QuestionOption("B", "Barat Laut", 0),
                QuestionOption("C", "Barat", 0),
                QuestionOption("D", "Tenggara", 0),
                QuestionOption("E", "Selatan", 0)
            ),
            correctKey = "A",
            explanation = "Perputaran sudut neto:\n+90° (searah) - 135° (berlawanan) = -45° (artinya berputar 45° berlawanan arah jarum jam).\nPosisi awal: Timur Laut (45° dari Utara).\nBerputar 45° berlawanan jarum jam dari Timur Laut menghasilkan posisi tepat menunjuk arah UTARA (0°).",
            fastTips = "Hitung selisih sudut: 90° - 135° = -45° (mundur 45° dari Timur Laut = Utara)."
        )
    )
}
