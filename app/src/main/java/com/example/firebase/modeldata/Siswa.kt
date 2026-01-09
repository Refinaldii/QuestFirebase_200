package com.example.firebase.modeldata

data class Siswa(
    val id: String = "",
    val nama: String = "",
    val alamat: String = "",
    val telpon: String = ""
)

data class DetailSiswa(
    val id: String = "",        // ✅ STRING
    val nama: String = "",
    val alamat: String = "",
    val telpon: String = ""
)



fun DetailSiswa.toDataSiswa(): Siswa =
    Siswa(
        id = id,
        nama = nama,
        alamat = alamat,
        telpon = telpon
    )

fun Siswa.toDetailSiswa(): DetailSiswa =
    DetailSiswa(
        id = id,
        nama = nama,
        alamat = alamat,
        telpon = telpon
    )



data class UIStateSiswa(
    val detailSiswa: DetailSiswa = DetailSiswa(),
    val isEntryValid: Boolean = false
)

fun Siswa.toUiStateSiswa(
    isEntryValid: Boolean = false
): UIStateSiswa =
    UIStateSiswa(
        detailSiswa = this.toDetailSiswa(),
        isEntryValid = isEntryValid
    )
