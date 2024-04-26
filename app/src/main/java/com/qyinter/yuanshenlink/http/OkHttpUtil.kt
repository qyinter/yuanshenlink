package com.qyinter.yuanshenlink.http

import android.os.Handler
import android.os.Message
import com.google.gson.Gson
import com.qyinter.yuanshenlink.dto.*
import com.qyinter.yuanshenlink.util.Md5Util
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLEncoder
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.math.floor

object HttpUtil {
    private var client = OkHttpClient().newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    fun getAuthKey(cookie: String, handler: Handler): String {
        thread {
            try {
                val time = Date().time
                val req = Request.Builder()
                    .url("https://webapi.account.mihoyo.com/Api/login_by_cookie?t=${time}")
                    .header("Cookie", cookie)
                    .build()
                val call = client.newCall(req)
                val response = call.execute()
                val loginCookieDataDtoBody = response.body?.string()
                println("body====>${loginCookieDataDtoBody}")
                val gson = Gson()
                val loginCookieData = gson.fromJson(
                    loginCookieDataDtoBody,
                    LoginCookieDataDto::class.java
                )
                val uid = loginCookieData.data.account_info.account_id
                val token = loginCookieData.data.account_info.weblogin_token

                //获取tid
                val multiReq = Request.Builder()
                    .url("https://api-takumi.mihoyo.com/auth/api/getMultiTokenByLoginTicket?login_ticket=${token}&token_types=3&uid=${uid}")
                    .header("Cookie", cookie)
                    .build()
                val multiCall = client.newCall(multiReq)
                val multiResponse = multiCall.execute()
                val multiDataDtoBody = multiResponse.body?.string()
                println(multiDataDtoBody)
                val multiDataData = gson.fromJson(
                    multiDataDtoBody,
                    LoginTokenDto::class.java
                )
                var newcookie = "stuid=${uid};"
                for (dataObj in multiDataData.data.list) {
                    newcookie += "${dataObj.name}=${dataObj.token};"
                }
                newcookie += cookie
                println(newcookie)
                //获取uid
                val uidReq = Request.Builder()
                    .url("https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=hk4e_cn")
                    .header("Cookie", newcookie)
                    .build()
                val uidCall = client.newCall(uidReq)
                val uidResponse = uidCall.execute()
                val uidDataDtoBody = uidResponse.body?.string()
                println("uidDataDtoBody:=>>>${uidDataDtoBody}")
                val userGameRolesByCookieData = gson.fromJson(
                    uidDataDtoBody,
                    UserGameRolesByCookieDataDto::class.java
                )
                val listUrl = ArrayList<ListUrl>()
                for (userService in userGameRolesByCookieData.data.list) {
                    val gameUid = userService.game_uid
                    val gameBiz = userService.game_biz
                    val region = userService.region
                    val authKeyPostData = AuthKeyPostData("webview_gacha", gameBiz, gameUid, region)
                    val toJson = gson.toJson(authKeyPostData)
                    val createRequestBody =
                        toJson.toRequestBody("application/json;charset=utf-8".toMediaType())
                    println(newcookie)
                    val authKeyReq = Request.Builder()
                        .url("https://api-takumi.mihoyo.com/binding/api/genAuthKey")
                        .header("Content-Type", "application/json;charset=utf-8")
                        .header("Host", "api-takumi.mihoyo.com")
                        .header("Accept", "application/json, text/plain, */*")
                        .header("x-rpc-app_version", "2.28.1")
                        .header("x-rpc-client_type", "5")
                        .header("x-rpc-device_id", "CBEC8312-AA77-489E-AE8A-8D498DE24E90")
                        .header("DS", getDs())
                        .header("Cookie", newcookie)
                        .post(createRequestBody)
                        .build()
                    val authKeyCall = client.newCall(authKeyReq)
                    val authKeyResponse = authKeyCall.execute()
                    val authKeyDataDtoBody = authKeyResponse.body?.string()
                    println(authKeyDataDtoBody)
                    val authKeyDataDto = gson.fromJson(
                        authKeyDataDtoBody,
                        AuthKeyDataDto::class.java
                    )
                    val authKey = URLEncoder.encode(authKeyDataDto.data.authkey, "utf-8")
                    val url =
                        "https://public-operation-hk4e.mihoyo.com/gacha_info/api/getGachaLog?win_mode=fullscreen&authkey_ver=1&sign_type=2&auth_appid=webview_gacha&init_type=301&gacha_id=b4ac24d133739b7b1d55173f30ccf980e0b73fc1&lang=zh-cn&device_type=mobile&game_version=CNRELiOS3.0.0_R10283122_S10446836_D10316937&plat_type=ios&game_biz=${gameBiz}&size=20&authkey=${authKey}&region=${region}&timestamp=1664481732&gacha_type=200&page=1&end_id=0"
                    println(url)
                    listUrl.add(ListUrl(gameUid, url))
                }
                val msg = Message.obtain()
                msg.obj = ChouKaObj(200, "请求成功", listUrl)
                //返回主线程
                handler.sendMessage(msg)
            } catch (e: java.lang.Exception) {
                val msg = Message.obtain()
                msg.obj = ChouKaObj(404, "获取失败", ArrayList())
                //返回主线程
                handler.sendMessage(msg)
            }
        }
        return ""
    }
    private fun getDs(): String {
        val salt = "ulInCDohgEs557j0VsPDYnQaaz6KJcv5"
        val time = Date().time / 1000
        val str = this.getStr()
        val key = "salt=${salt}&t=${time}&r=${str}"
        val md5 = Md5Util.getMD5(key)

        return "${time},${str},${md5}"
    }

    private fun getStr(): String {
        val chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678"
        val maxPos = chars.length
        var code = ""
        for (i in 0..5) {
            code += chars[floor(Math.random() * maxPos).toInt()]
        }
        return code
    }
}