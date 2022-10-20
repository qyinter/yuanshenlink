package com.qyinter.yuanshenlink

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.qyinter.yuanshenlink.dto.ChouKaObj
import com.qyinter.yuanshenlink.http.HttpUtil

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.domStorageEnabled = true

        myWebView.loadUrl("https://user.mihoyo.com")
        val handle: Handler = object : Handler(Looper.getMainLooper()) {
            @SuppressLint("HandlerLeak")
            override fun handleMessage(msg: Message) {
                //正常操作
                msg.let {
                    val obj: ChouKaObj = msg.obj as ChouKaObj
                    if (obj.code == 200) {
                        val editText = findViewById<EditText>(R.id.input)
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

                        if (obj.urlListObj.size > 1) {
                            val array = Array(obj.urlListObj.size) { "" }
                            for ((index, value) in obj.urlListObj.withIndex()) {
                                array[index] = value.uid
                            }
                            builder.setIcon(R.drawable.ic_launcher_foreground)
                                .setTitle("选择你想要查询的账号")
                            builder.setItems(
                                array
                            ) { _, which ->
                                for (listUrl in obj.urlListObj) {
                                    if (listUrl.uid == array[which]) {
                                        editText.setText(listUrl.url)
                                        val cm =
                                            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        //clipData中的this就是需要复制的文本
                                        val clipData = ClipData.newPlainText("", listUrl.url)
                                        cm.setPrimaryClip(clipData)
                                        Toast.makeText(
                                            this@MainActivity,
                                            "已复制到剪贴板",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                Toast.makeText(
                                    applicationContext,
                                    "你选择了Uid:" + array[which],
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            val alert = builder.create()
                            alert.show()
                        } else {
                            editText.setText(obj.urlListObj[0].url)
                            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            //clipData中的this就是需要复制的文本
                            val clipData = ClipData.newPlainText("", obj.urlListObj[0].url)
                            cm.setPrimaryClip(clipData)
                            Toast.makeText(this@MainActivity, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "请先登录米游社", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        R.id.cookieBtn.onClick(this) {
            val instance = CookieManager.getInstance()
            val cookie = instance.getCookie("https://user.mihoyo.com")
            HttpUtil.getAuthKey(cookie, handle)
        }
    }
}

fun Int.onClick(activity: Activity, click: () -> Unit) {
    activity.findViewById<View>(this).setOnClickListener {
        click()
    }
}

