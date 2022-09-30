package com.qyinter.yuanshenlink.dto

data class LoginCookieDataDto(
    val code: Int,
    val `data`: Data
)

data class Data(
    val account_info: AccountInfo,
    val game_ctrl_info: Any,
    val msg: String,
    val notice_info: NoticeInfo,
    val status: Int
)

data class AccountInfo(
    val account_id: Int,
    val create_time: Int,
    val email: String,
    val identity_code: String,
    val is_adult: Int,
    val is_email_verify: Int,
    val real_name: String,
    val safe_area_code: String,
    val safe_level: Int,
    val safe_mobile: String,
    val weblogin_token: String
)

class NoticeInfo