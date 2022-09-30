package com.qyinter.yuanshenlink.dto

data class LoginTokenDto(
    val `data`: LoginTokenData,
    val message: String,
    val retcode: Int
)

data class LoginTokenData(
    val list: List<DataObj>
)

data class DataObj(
    val name: String,
    val token: String
)