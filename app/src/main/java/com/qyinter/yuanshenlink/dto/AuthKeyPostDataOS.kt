package com.qyinter.yuanshenlink.dto

data class AuthKeyPostDataOS(
    val game_biz: String,
    val game_uid: String,
    val region: String,
    val nickname: String,
    val level: Int,
    val is_official: Boolean,
    val region_name: String,
    val is_chosen: Boolean,
    val auth_appid: String = "webview_gacha",
    val win_mode:String = "fullscreen",
    val authkey_ver:Int = 1,
    val sign_type:Int = 2,
    val device_type:String = "mobile",
    val plat_type:String = "android",
    val game_version:String = "OSRELAndroid2.3.0_R10676272_S10805493_D10772333"
)