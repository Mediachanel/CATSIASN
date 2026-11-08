package com.example.data.seed

import com.example.data.model.ExamCategory
import com.example.data.model.Question
import com.example.data.model.QuestionOption

object TwkQuestionBank {

    val questions: List<Question> = listOf(
        Question(
            id = "twk_pilar_pancasila_1",
            packageId = "pool",
            number = 1,
            category = ExamCategory.TWK,
            subCategory = "Pilar Negara (Pancasila)",
            questionText = "Seorang pejabat publik menolak intervensi dari kerabat dekatnya yang meminta bantuan meloloskan proyek tender tanpa melalui mekanisme lelang resmi. Pejabat tersebut menegaskan bahwa semua peserta tender berhak mendapatkan perlakuan yang adil dan setara di depan hukum. Sikap pejabat tersebut mencerminkan pengamalan Pancasila, terutama sila...",
            options = listOf(
                QuestionOption("A", "Kemanusiaan yang adil dan beradab", 0),
                QuestionOption("B", "Persatuan Indonesia", 0),
                QuestionOption("C", "Keadilan sosial bagi seluruh rakyat Indonesia", 5),
                QuestionOption("D", "Ketuhanan Yang Maha Esa", 0),
                QuestionOption("E", "Kerakyatan yang dipimpin oleh hikmat kebijaksanaan dalam permusyawaratan/perwakilan", 0)
            ),
            correctKey = "C",
            explanation = "Menjamin hak yang sama, tidak diskriminatif dalam pengelolaan sumber daya publik, dan mencegah nepotisme demi keadilan publik merupakan butir pengamalan Sila Kelima: Keadilan Sosial bagi Seluruh Rakyat Indonesia.",
            fastTips = "Kata kunci Sila ke-5: Keadilan distribusi publik, anti korupsi/nepotisme anggaran, perlakuan adil dalam hak ekonomi/peluang masyarakat."
        ),
        Question(
            id = "twk_pilar_pancasila_2",
            packageId = "pool",
            number = 2,
            category = ExamCategory.TWK,
            subCategory = "Pilar Negara (Pancasila)",
            questionText = "Pancasila sebagai ideologi terbuka memiliki tiga dimensi utama, yaitu dimensi realitas, dimensi idealisme, dan dimensi fleksibilitas. Maksud dari dimensi fleksibilitas adalah...",
            options = listOf(
                QuestionOption("A", "Nilai-nilai Pancasila dapat diubah dan disesuaikan secara bebas oleh penguasa sesuai kebutuhan politik", 0),
                QuestionOption("B", "Pancasila memiliki keluwesan yang memungkinkan pengembangan pemikiran baru yang relevan tanpa mengubah nilai dasarnya", 5),
                QuestionOption("C", "Masyarakat bebas menafsirkan Pancasila sesuai dengan norma adat kelompoknya masing-masing", 0),
                QuestionOption("D", "Pancasila dapat digantikan dengan ideologi lain jika kondisi zaman menuntutnya", 0),
                QuestionOption("E", "Hukum positif di Indonesia tidak harus berpedoman pada sila-sila Pancasila", 0)
            ),
            correctKey = "B",
            explanation = "Dimensi fleksibilitas bermakna Pancasila memiliki daya lentur dan keterbukaan intelektual untuk menjawab tantangan zaman dan perkembangan iptek tanpa sedikit pun merombak nilai dasar (ontologis) yang terkandung di dalamnya.",
            fastTips = "Tiga Dimensi Ideologi Terbuka: Realitas (hidup di masyarakat), Idealisme (harapan/cita-cita), Fleksibilitas (adaptif terhadap perkembangan tanpa merubah substansi dasar)."
        ),
        Question(
            id = "twk_pilar_uud_1",
            packageId = "pool",
            number = 3,
            category = ExamCategory.TWK,
            subCategory = "Pilar Negara (UUD 1945)",
            questionText = "Pasal 24C Ayat (1) UUD NRI Tahun 1945 mengatur kewenangan Mahkamah Konstitusi (MK). Di antara opsi berikut, yang BUKAN merupakan kewenangan MK adalah...",
            options = listOf(
                QuestionOption("A", "Menguji undang-undang terhadap Undang-Undang Dasar", 0),
                QuestionOption("B", "Memutus sengketa kewenangan lembaga negara yang kewenangannya diberikan oleh UUD", 0),
                QuestionOption("C", "Memutus pembubaran partai politik", 0),
                QuestionOption("D", "Memutus perselisihan tentang hasil pemilihan umum", 0),
                QuestionOption("E", "Menguji peraturan perundang-undangan di bawah undang-undang terhadap undang-undang", 5)
            ),
            correctKey = "E",
            explanation = "Menguji peraturan di bawah UU terhadap UU adalah kewenangan Mahkamah Agung (MA) berdasarkan Pasal 24A ayat (1). Kewenangan MK adalah menguji UU terhadap UUD, sengketa kewenangan lembaga negara, pembubaran parpol, memutus PHPU, dan memberi putusan atas dugaan pelanggaran Presiden/Wapres.",
            fastTips = "Uji materi UU vs UUD = MK. Uji materi Peraturan di bawah UU (PP, Perpres, Perda) vs UU = MA!"
        ),
        Question(
            id = "twk_pilar_uud_2",
            packageId = "pool",
            number = 4,
            category = ExamCategory.TWK,
            subCategory = "Pilar Negara (UUD 1945)",
            questionText = "Amandemen UUD NRI 1945 berlangsung sebanyak empat kali kurun waktu 1999 hingga 2002. Salah satu kesepakatan dasar Panitia Ad Hoc MPR dalam melakukan amandemen UUD 1945 adalah...",
            options = listOf(
                QuestionOption("A", "Mengubah Pembukaan UUD 1945 agar selaras dengan piagam PBB", 0),
                QuestionOption("B", "Tidak mengubah Pembukaan UUD 1945 dan tetap mempertahankan NKRI", 5),
                QuestionOption("C", "Mengubah sistem pemerintahan presidensial menjadi sistem parlementer", 0),
                QuestionOption("D", "Memasukkan penjelasan resmi UUD 1945 ke dalam naskah terpisah", 0),
                QuestionOption("E", "Menghapuskan kedudukan MPR sebagai lembaga negara", 0)
            ),
            correctKey = "B",
            explanation = "5 Kesepakatan Dasar Amandemen UUD 1945: (1) Tidak mengubah Pembukaan UUD 1945, (2) Tetap mempertahankan NKRI, (3) Mempertegas sistem presidensial, (4) Menghapus Penjelasan UUD 1945 dan memasukkan hal normatif ke pasal-pasal, (5) Dilakukan secara adendum.",
            fastTips = "Kesepakatan sakti: Pembukaan & Bentuk NKRI adalah HARGA MATI (tidak boleh diubah sama sekali)."
        ),
        Question(
            id = "twk_integritas_1",
            packageId = "pool",
            number = 5,
            category = ExamCategory.TWK,
            subCategory = "Integritas & Anti Korupsi",
            questionText = "Dalam UU No. 31 Tahun 1999 jo. UU No. 20 Tahun 2001, tindak pidana korupsi dikelompokkan ke dalam 7 jenis delik korupsi. Tindakan seorang bendahara dinas yang dengan sengaja menggelapkan atau memalsukan buku administrasi keuangan kantor termasuk ke dalam delik...",
            options = listOf(
                QuestionOption("A", "Merugikan Keuangan Negara", 0),
                QuestionOption("B", "Penggelapan dalam Jabatan", 5),
                QuestionOption("C", "Benturan Kepentingan dalam Pengadaan", 0),
                QuestionOption("D", "Perbuatan Curang", 0),
                QuestionOption("E", "Pemerasan", 0)
            ),
            correctKey = "B",
            explanation = "Penggelapan dalam jabatan (Pasal 8, 9, 10) meliputi perbuatan pegawai negeri yang menggelapkan uang/surat berharga, memalsukan buku daftar untuk pemeriksaan administrasi, atau membiarkan orang lain merusak akta/buku dinas.",
            fastTips = "Korupsi pemalsuan buku/administrasi oleh bendahara/staf resmi = Penggelapan dalam Jabatan."
        ),
        Question(
            id = "twk_integritas_2",
            packageId = "pool",
            number = 6,
            category = ExamCategory.TWK,
            subCategory = "Integritas & Anti Korupsi",
            questionText = "Menurut Peraturan KPK No. 2 Tahun 2019, batas waktu maksimal bagi seorang penyelenggara negara untuk melaporkan gratifikasi yang diterimanya kepada KPK terhitung sejak tanggal diterimanya gratifikasi adalah...",
            options = listOf(
                QuestionOption("A", "7 hari kerja", 0),
                QuestionOption("B", "14 hari kerja", 0),
                QuestionOption("C", "30 hari kerja", 5),
                QuestionOption("D", "60 hari kalender", 0),
                QuestionOption("E", "90 hari kerja", 0)
            ),
            correctKey = "C",
            explanation = "Berdasarkan Pasal 12C UU Tindak Pidana Korupsi, ketentuan pidana gratifikasi tidak berlaku jika penerima melaporkan kepada KPK paling lambat 30 (tiga puluh) hari kerja terhitung sejak tanggal gratifikasi diterima.",
            fastTips = "Hafal angka: Pelaporan Gratifikasi ke KPK = 30 Hari Kerja!"
        ),
        Question(
            id = "twk_bela_negara_1",
            packageId = "pool",
            number = 7,
            category = ExamCategory.TWK,
            subCategory = "Bela Negara",
            questionText = "Konsep pertahanan negara Indonesia menganut Sistem Pertahanan dan Keamanan Rakyat Semesta (Sishankamrata) yang bercirikan kerakyatan, kesemestaan, dan kewilayahan. Yang dimaksud dengan ciri 'kesemestaan' adalah...",
            options = listOf(
                QuestionOption("A", "Kekuatan militer ditempatkan merata di seluruh pulau-pulau terluar Indonesia", 0),
                QuestionOption("B", "Seluruh sumber daya nasional dan sarana prasarana didayagunakan secara total bagi upaya pertahanan negara", 5),
                QuestionOption("C", "Orientasi pertahanan diabdikan semata-mata untuk kepentingan kesejahteraan rakyat", 0),
                QuestionOption("D", "Rakyat diwajibkan memegang senjata api dan berpatroli mandiri setiap hari", 0),
                QuestionOption("E", "Menjalin aliansi pertahanan militer dengan negara-negara adidaya", 0)
            ),
            correctKey = "B",
            explanation = "3 Ciri Sishankamrata:\n1. Kerakyatan: Orientasi pertahanan untuk kepentingan seluruh rakyat.\n2. Kesemestaan: Seluruh sumber daya nasional didayagunakan untuk pertahanan.\n3. Kewilayahan: Gelar kekuatan pertahanan bertumpu pada geografi seluruh wilayah NKRI.",
            fastTips = "Kesemestaan = Semua sumber daya, sarana, dan infrastruktur bangsa ikut berkontribusi."
        ),
        Question(
            id = "twk_bela_negara_2",
            packageId = "pool",
            number = 8,
            category = ExamCategory.TWK,
            subCategory = "Bela Negara",
            questionText = "Salah satu ancaman non-militer berdimensi ideologi yang paling mengancam kedaulatan bangsa di era modern adalah...",
            options = listOf(
                QuestionOption("A", "Pelanggaran wilayah udara oleh pesawat tempur asing", 0),
                QuestionOption("B", "Penyebaran propaganda radikalisme dan intoleransi melalui media sosial yang mendiskreditkan dasar negara", 5),
                QuestionOption("C", "Serangan kapal perang bajak laut di selat Malaka", 0),
                QuestionOption("D", "Penyelundupan komoditas tambang ilegal di perbatasan darat", 0),
                QuestionOption("E", "Sengketa tapal batas pulau karang di laut Natuna Utara", 0)
            ),
            correctKey = "B",
            explanation = "Ancaman ideologi non-militer adalah infiltrasi paham radikal, ekstremisme berbasis agama/golongan, dan intoleransi yang berupaya merongrong Pancasila melalui teknologi informasi dan media sosial.",
            fastTips = "Ancaman ideologi = menyerang pemikiran, nilai Pancasila, dan integrasi persatuan bangsa."
        ),
        Question(
            id = "twk_nasionalisme_1",
            packageId = "pool",
            number = 9,
            category = ExamCategory.TWK,
            subCategory = "Nasionalisme",
            questionText = "Peristiwa Rengasdengklok pada tanggal 16 Agustus 1945 dipicu oleh adanya perbedaan pandangan antara golongan muda dan golongan tua mengenai...",
            options = listOf(
                QuestionOption("A", "Penetapan lagu Indonesia Raya sebagai lagu kebangsaan resmi", 0),
                QuestionOption("B", "Waktu dan cara pelaksanaan proklamasi kemerdekaan Indonesia agar murni tanpa campur tangan Jepang", 5),
                QuestionOption("C", "Tokoh yang berhak membacakan teks naskah proklamasi kemerdekaan", 0),
                QuestionOption("D", "Bentuk negara antara monarki konstitusional atau republik", 0),
                QuestionOption("E", "Lokasi pelaksanaan pembacaan proklamasi di Lapangan Ikada atau Pegangsaan Timur", 0)
            ),
            correctKey = "B",
            explanation = "Golongan muda (Sukarni, Chaerul Saleh, dll.) mendesak proklamasi segera dilaksanakan mandiri tanpa persetujuan PPKI/Jepang, sedangkan golongan tua menginginkan proklamasi dibahas secara tertib melalui sidang PPKI.",
            fastTips = "Inti Rengasdengklok: Golongan muda ingin proklamasi murni perjuangan bangsa tanpa 'hadiah' janji Jepang."
        ),
        Question(
            id = "twk_nasionalisme_2",
            packageId = "pool",
            number = 10,
            category = ExamCategory.TWK,
            subCategory = "Nasionalisme",
            questionText = "Prinsip Chauvinisme adalah bentuk penyimpangan nasionalisme yang berbahaya karena...",
            options = listOf(
                QuestionOption("A", "Mencintai budaya bangsa sendiri tanpa mau mempelajari bahasa asing", 0),
                QuestionOption("B", "Menolak membeli produk impor untuk melindungi pengrajin UMKM dalam negeri", 0),
                QuestionOption("C", "Mengagungkan bangsa sendiri secara berlebihan sambil memandang rendah martabat bangsa lain", 5),
                QuestionOption("D", "Mengutamakan kepentingan musyawarah daripada pemungutan suara", 0),
                QuestionOption("E", "Mengalokasikan anggaran negara untuk pertahanan perbatasan laut", 0)
            ),
            correctKey = "C",
            explanation = "Chauvinisme adalah rasa cinta tanah air yang sempit dan berlebihan (ekstrem) yang disertai sikap merendahkan bangsa lain. Contoh historis: fasisme Jerman era Hitler.",
            fastTips = "Nasionalisme positif = cinta bangsa sendiri dan menghormati bangsa lain. Chauvinisme = mengagungkan diri dan merendahkan bangsa lain."
        ),
        Question(
            id = "twk_bhinneka_1",
            packageId = "pool",
            number = 11,
            category = ExamCategory.TWK,
            subCategory = "Bhinneka Tunggal Ika",
            questionText = "Sikap etnosentrisme dalam kehidupan masyarakat majemuk dapat menjadi pemicu disintegrasi bangsa karena...",
            options = listOf(
                QuestionOption("A", "Masyarakat bangga mengenakan pakaian adat sukunya dalam upacara nasional", 0),
                QuestionOption("B", "Menilai kebudayaan kelompok lain hanya berdasarkan standar dan sudut pandang budayanya sendiri serta menganggap sukunya paling unggul", 5),
                QuestionOption("C", "Mengadakan festival kesenian daerah lintas provinsi secara berkala", 0),
                QuestionOption("D", "Mengembangkan bahasa daerah sebagai muatan lokal di kurikulum sekolah", 0),
                QuestionOption("E", "Mendirikan paguyuban kerukunan warga perantauan di ibu kota", 0)
            ),
            correctKey = "B",
            explanation = "Etnosentrisme adalah kecenderungan memandang budaya kelompoknya sendiri sebagai tolak ukur mutlak dan superior terhadap kelompok etnis lain, yang berpotensi memicu prasangka, diskriminasi, dan konflik sosial.",
            fastTips = "Etnosentrisme = suku/budaya sendiri dianggap paling benar dan suku lain dianggap rendah."
        ),
        Question(
            id = "twk_nkri_1",
            packageId = "pool",
            number = 12,
            category = ExamCategory.TWK,
            subCategory = "NKRI & Ketatanegaraan",
            questionText = "Dekrit Presiden 5 Juli 1959 yang dikeluarkan oleh Presiden Soekarno dilatarbelakangi oleh kegagalan Badan Konstituante dalam...",
            options = listOf(
                QuestionOption("A", "Menyelenggarakan pemilihan umum legislatif pertama", 0),
                QuestionOption("B", "Menetapkan undang-undang dasar baru pengganti UUDS 1950", 5),
                QuestionOption("C", "Menumpas pemberontakan PRRI dan Permesta di daerah", 0),
                QuestionOption("D", "Menyelenggarakan Konferensi Asia Afrika di Bandung", 0),
                QuestionOption("E", "Memilih menteri-menteri dalam kabinet koalisi partai", 0)
            ),
            correctKey = "B",
            explanation = "Isi Dekrit Presiden 5 Juli 1959:\n1. Pembubaran Konstituante\n2. Berlakunya kembali UUD 1945 dan tidak berlakunya lagi UUDS 1950\n3. Pembentukan MPRS dan DPAS.",
            fastTips = "Penyebab Dekrit 5 Juli 1959: Konstituante jalan buntu (deadlock) merumuskan UUD baru."
        ),
        Question(
            id = "twk_bhs_indonesia_1",
            packageId = "pool",
            number = 13,
            category = ExamCategory.TWK,
            subCategory = "Bahasa Indonesia",
            questionText = "Manakah kalimat di bawah ini yang paling tepat dan hemat penggunaan kata (pleonasme) sesuai kaidah bahasa Indonesia baku?",
            options = listOf(
                QuestionOption("A", "Para hadirin sekalian dimohon untuk segera berdiri menyanyikan lagu Indonesia Raya.", 0),
                QuestionOption("B", "Pemerintah mulai mensosialisasikan kebijakan baru mengenai subsidi energi hijau.", 0),
                QuestionOption("C", "Banyak anak-anak bermain bola di lapangan saat sore hari.", 0),
                QuestionOption("D", "Peserta ujian wajib membawa kartu identitas resmi pada saat memasuki ruangan tes.", 5),
                QuestionOption("E", "Keluarga korban sangat amat terpukul mendengar kabar kecelakaan tersebut.", 0)
            ),
            correctKey = "D",
            explanation = "Opsi A pleonasme ('para' + 'sekalian'). Opsi B salah ejaan bentukan kata ('mensosialisasikan' seharusnya 'menyosialisasikan' karena huruf KTSP luluh). Opsi C pleonasme ('banyak' + kata ulang 'anak-anak'). Opsi E pleonasme ('sangat amat'). Opsi D adalah kalimat efektif dan baku.",
            fastTips = "Ingat hukum KTSP luluh: K, T, S, P luluh jika didahului awalan me- diikuti vokal. (me- + sosialisasikan = menyosialisasikan)."
        ),
        Question(
            id = "twk_bhs_indonesia_2",
            packageId = "pool",
            number = 14,
            category = ExamCategory.TWK,
            subCategory = "Bahasa Indonesia",
            questionText = "Bacalah kutipan paragraf berikut:\n'Transformasi digital sektor pelayanan publik bukan lagi sekadar pilihan, melainkan sebuah keharusan mutlak. Dengan memanfaatkan teknologi informasi, birokrasi pemerintahan dapat memangkas mata rantai administrasi yang berbelit-belit, meningkatkan transparansi anggaran, dan mencegah potensi pungutan liar.'\nGagasan pokok paragraf di atas adalah...",
            options = listOf(
                QuestionOption("A", "Potensi pungutan liar yang merugikan keuangan negara", 0),
                QuestionOption("B", "Kewajiban transformasi digital dalam pelayanan publik", 5),
                QuestionOption("C", "Rantai birokrasi pemerintahan yang berbelit-belit", 0),
                QuestionOption("D", "Pengurangan anggaran belanja teknologi informasi", 0),
                QuestionOption("E", "Penurunan kinerja aparatur sipil negara di era modern", 0)
            ),
            correctKey = "B",
            explanation = "Ide pokok terletak pada kalimat pertama (paragraf deduktif): perlunya atau kewajiban mutlak transformasi digital pada sektor pelayanan publik. Kalimat-kalimat berikutnya adalah penjelas manfaatnya.",
            fastTips = "Ide pokok biasanya di kalimat pertama (deduktif) atau kalimat terakhir (induktif). Cari inti pembahasan utama."
        )
    )
}
