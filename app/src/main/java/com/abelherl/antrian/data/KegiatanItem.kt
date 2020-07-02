package com.abelherl.antrian.data

import javax.xml.transform.Source

data class KegiatanItem(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: String,
    val estimation: Int,
    val start: String,
    val end: String,
    val status: Int
)