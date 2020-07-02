package com.abelherl.antrian.data

data class AntrianItem (
    val id: Int,
    val userId: String,
    val kegiatanId: Int,
    val beginAt: String,
    val status: Int
)