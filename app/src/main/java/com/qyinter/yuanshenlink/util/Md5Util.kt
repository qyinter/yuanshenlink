package com.qyinter.yuanshenlink.util

import java.math.BigInteger
import java.security.MessageDigest

object Md5Util {
    /**
     * 对字符串 MD5 加密
     *
     * @param str 原始值
     * @return MD5 值
     */
    fun getMD5(str: String): String? {
        try {
            // 生成一个MD5加密计算摘要
            val md = MessageDigest.getInstance("MD5")
            // 计算md5函数
            md.update(str.toByteArray())
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            var result = BigInteger(1, md.digest()).toString(16)
            while (result.length < 32) {
                result = "0$result"
            }
            return result
        } catch (e: Exception) {
            println(e)
        }
        return null
    }
}