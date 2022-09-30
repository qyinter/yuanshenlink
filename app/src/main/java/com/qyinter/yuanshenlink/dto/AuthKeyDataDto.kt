package com.qyinter.yuanshenlink.dto

data class AuthKeyDataDto(
    val `data`: AuthKeyData,
    val message: String,
    val retcode: Int
)

data class AuthKeyData(
    val authkey: String,
    val authkey_ver: Int,
    val sign_type: Int
)