package com.qyinter.yuanshenlink.dto

data class UserGameRolesByCookieDataDto(
    val `data`: UserGameRolesByCookieData,
    val message: String,
    val retcode: Int
)

data class UserGameRolesByCookieData(
    val list: List<UserService>
)

data class UserService(
    val game_biz: String,
    val game_uid: String,
    val is_chosen: Boolean,
    val is_official: Boolean,
    val level: Int,
    val nickname: String,
    val region: String,
    val region_name: String,
)