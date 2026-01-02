package com.example.firebase.modeldata


data class Siswa (
    val id : Long = 0,
    val nama : String = "",
    val alamat : String = "",
    val telpon : String = ""
)

data class DetailSiswa (
    val id : Long = 0,
)