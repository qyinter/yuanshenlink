package com.qyinter.yuanshenlink.dto

data class ChouKaObj(
    val code: Int,
    val msg: String,
    val urlListObj: List<ListUrl>,
)
data class ListUrl(
    val uid: String,
    val url: String,
)