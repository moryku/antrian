package com.abelherl.antrian.data

import java.util.*
import javax.xml.transform.Source

data class KegiatanItem(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: Date,
    val estimation: Int,
    val start: Date,
    val end: Date,
    val status: Int
)