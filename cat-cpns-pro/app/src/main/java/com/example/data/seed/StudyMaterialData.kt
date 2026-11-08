package com.example.data.seed

import com.example.data.model.ExamCategory
import com.example.data.model.LeaderboardItem

data class StudyMaterial(
    val id: String,
    val title: String,
    val category: ExamCategory,
    val readTimeMinutes: Int,
    val summary: String,
    val keyPoints: List<String>,
    val cheatFormulas: List<String>
)

object StudyMaterialData {

    val materials: List<StudyMaterial> = listOf(
        StudyMaterial(
            id = "mat_twk_pilar",
            title = "Kunci Pengamalan Butir Pancasila & UUD 1945",
            category = ExamCategory.TWK,
            readTimeMinutes = 6,
            summary = "Pahami perbedaan tipis antara Sila ke-2 (kemanusiaan & moralitas individu) dan Sila ke-5 (keadilan sosial masyarakat luas), serta pasal krusial hasil amandemen I-IV.",
            keyPoints = listOf(
                "Sila 1: Toleransi beragama, tidak memaksakan agama/kepercayaan kepada orang lain.",
                "Sila 2: Harkat martabat kemanusiaan, tenggang rasa, anti-perundungan, menjunjung tinggi kebenaran dan keadilan moral.",
                "Sila 3: Cinta tanah air, rela berkorban, menjaga keutuhan NKRI, bangga produk dalam negeri.",
                "Sila 4: Musyawarah mufakat, tidak memaksakan kehendak, menghormati hasil keputusan bersama.",
                "Sila 5: Keadilan sosial, tidak bergaya hidup mewah, suka bekerja keras, menghargai karya orang lain, hak milik sosial."
            ),
            cheatFormulas = listOf(
                "Trik Sila 2 vs Sila 5: Jika terkait individu/adab/perilaku sopan santun -> Sila 2. Jika terkait kesejahteraan sosial/ekonomi/fasilitas umum/pemberian hak masyarakat -> Sila 5.",
                "UUD 1945: 4 Kali Amandemen (1999, 2000, 2001, 2002). Lembaga baru pasca amandemen: DPD, MK, KY.",
                "Pasal Sakti: Pasal 1 (Bentuk & Kedaulatan), Pasal 27 (Kesetaraan hukum & pekerjaan), Pasal 30 (Pertahanan Keamanan Sishankamrata), Pasal 33 (Perekonomian Nasional)."
            )
        ),
        StudyMaterial(
            id = "mat_twk_bela_negara",
            title = "Mastery Bela Negara & 9 Nilai Integritas KPK",
            category = ExamCategory.TWK,
            readTimeMinutes = 5,
            summary = "Konsep bela negara modern non-militer melalui pengabdian profesi serta 9 pilar integritas wajib bagi calon ASN.",
            keyPoints = listOf(
                "5 Nilai Dasar Bela Negara: Cinta Tanah Air, Kesadaran Berbangsa dan Bernegara, Setia pada Pancasila, Rela Berkorban, Kemampuan Awal Bela Negara (fisik & psikis).",
                "Bela negara ASN diwujudkan melalui pelayanan prima, netralitas politik, dan loyalitas terhadap konstitusi negara.",
                "9 Nilai Integritas KPK terbagi menjadi: Nilai Inti (Jujur, Tanggung Jawab, Disiplin), Nilai Sikap (Adil, Berani, Peduli), dan Nilai Etos Kerja (Kerja Keras, Mandiri, Sederhana)."
            ),
            cheatFormulas = listOf(
                "Hafalkan Jembatan Keledai: JuTangDis (Inti), ABePe (Sikap), KeManSe (Etos Kerja).",
                "Gratifikasi: Penerimaan dalam arti luas (uang, diskon, komisi, tiket) terkait jabatan. Wajib dilaporkan ke KPK maksimal 30 hari kerja!"
            )
        ),
        StudyMaterial(
            id = "mat_tiu_rumus_cepat",
            title = "Rumus Sakti TIU: Deret, Silogisme & Berhitung Cepat",
            category = ExamCategory.TIU,
            readTimeMinutes = 8,
            summary = "Strategi menyelesaikan soal matematika dan silogisme dalam waktu kurang dari 40 detik per soal tanpa rumus panjang.",
            keyPoints = listOf(
                "Pola Deret: Selalu cek selisih pertama (+, -, x, /), jika tidak teratur cek deret loncat 1 atau loncat 2 angka.",
                "Silogisme BKN: 'Semua + Sebagian = Sebagian'. Jika ada kata 'Tidak ada / Bukan', perhatikan negasi.",
                "Analitis: Buat garis urutan mendatar atau tabel eliminasi langsung di kertas buram, jangan hanya membayangkan di kepala."
            ),
            cheatFormulas = listOf(
                "Rumus Pekerja Terhenti: Tambahan Pekerja = (Hari Libur x Pekerja Awal) / Sisa Hari.",
                "Rumus Berpapasan: Waktu = Jarak Total / (Kecepatan A + Kecepatan B).",
                "Rumus Menyusul: Waktu = Selisih Jarak Awal / (Kecepatan 2 - Kecepatan 1).",
                "Pecahan Ajaib: 1/8 = 12.5% (0.125), 3/8 = 37.5% (0.375), 1/6 = 16.67%, 5/6 = 83.33%."
            )
        ),
        StudyMaterial(
            id = "mat_tkp_poin5",
            title = "Mindset Jawaban Poin 5 TKP (Core Values ASN BerAKHLAK)",
            category = ExamCategory.TKP,
            readTimeMinutes = 7,
            summary = "Metode sistematis memilih opsi dengan bobot 5 sempurna berdasarkan karakter ASN BerAKHLAK resmi MenPAN-RB.",
            keyPoints = listOf(
                "Berorientasi Pelayanan: Utamakan kepentingan masyarakat di atas kepentingan pribadi/kelompok, ramah, cekatan, solutif.",
                "Akuntabel: Melaksanakan tugas dengan jujur, bertanggung jawab, cermat, disiplin, tidak menyalahgunakan kewenangan jabatan.",
                "Kompeten: Terus belajar, mengembangkan kapabilitas diri, membantu orang lain belajar.",
                "Harmonis: Menghargai setiap orang apapun latar belakangnya, suka menolong, membangun lingkungan kerja kondusif.",
                "Loyal: Memegang teguh ideologi Pancasila dan UUD 1945, setia kepada NKRI dan pemerintahan yang sah.",
                "Adaptif: Cepat menyesuaikan diri menghadapi perubahan zaman dan digitalisasi TIK.",
                "Kolaboratif: Memberi kesempatan kepada berbagai pihak untuk berkontribusi dan bersinergi."
            ),
            cheatFormulas = listOf(
                "Trik Menemukan Opsi Poin 5:\n1. Mengandung solusi konkret (bukan sekadar niat atau menyalahkan orang lain).\n2. Memiliki dampak positif jangka panjang bagi organisasi/masyarakat.\n3. Menunjukkan inisiatif tinggi tanpa melangkahi etika kedinasan.\n4. Dalam kasus Anti-Radikalisme: Tegas, setia pada NKRI, tidak apatis/toleran pada paham intoleran."
            )
        )
    )

    fun getSimulatedLeaderboard(userTotalScore: Int = 0): List<LeaderboardItem> {
        val baseList = mutableListOf(
            LeaderboardItem(1, "Rian Prasetyo, S.Kom.", "CPNS-2026-90412", "Kemenkeu - Ditjen Pajak", 130, 160, 212, 502, true),
            LeaderboardItem(2, "Dian Permatasari, S.H.", "CPNS-2026-88129", "Kemenkumham - Analis Hukum", 125, 155, 209, 489, true),
            LeaderboardItem(3, "Budi Santoso, S.T.", "CPNS-2026-73190", "BKN - Pranata Komputer", 120, 165, 198, 483, true),
            LeaderboardItem(4, "Nadia Salsabila, S.E.", "CPNS-2026-55410", "Kemenlu - Diplomat Ahli", 115, 150, 208, 473, true),
            LeaderboardItem(5, "Faisal Ramadhan, S.Sos.", "CPNS-2026-44319", "Kemendikbud - Analis Kebijakan", 110, 155, 204, 469, true),
            LeaderboardItem(6, "Citra Dewi Anggraini, S.Kep.", "CPNS-2026-38291", "Kemenkes - Tenaga Medis", 105, 145, 210, 460, true),
            LeaderboardItem(7, "Agus Hermawan, S.P.", "CPNS-2026-29103", "Kementan - Pengawas Mutu", 115, 140, 202, 457, true),
            LeaderboardItem(8, "Putri Ayuningtyas, S.Si.", "CPNS-2026-19284", "BMKG - Pengamat Meteorologi", 100, 150, 201, 451, true),
            LeaderboardItem(9, "Hendro Wicaksono, S.T.", "CPNS-2026-10294", "PUPR - Ahli Teknik Pengairan", 110, 135, 198, 443, true),
            LeaderboardItem(10, "Maya Safitri, S.Pd.", "CPNS-2026-09182", "Pemprov DKI - Guru Ahli Pertama", 95, 140, 205, 440, true)
        )

        if (userTotalScore > 0) {
            val userRank = baseList.count { it.totalScore > userTotalScore } + 1
            val userItem = LeaderboardItem(
                rank = userRank,
                participantName = "Anda (Simulasi Peserta)",
                regNumber = "SIM-2026-ONLINE",
                targetFormation = "Target Formasi Impian",
                twkScore = (userTotalScore * 0.28).toInt(),
                tiuScore = (userTotalScore * 0.35).toInt(),
                tkpScore = userTotalScore - (userTotalScore * 0.28).toInt() - (userTotalScore * 0.35).toInt(),
                totalScore = userTotalScore,
                isPassed = userTotalScore >= 311,
                isCurrentUser = true
            )
            baseList.add(userItem)
        }

        return baseList.sortedByDescending { it.totalScore }.mapIndexed { index, item ->
            item.copy(rank = index + 1)
        }
    }
}
