package com.qyinter.yuanshenlink

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.widget.EditText
import android.widget.Toast
import com.qyinter.yuanshenlink.http.HttpUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.domStorageEnabled = true
        myWebView.loadUrl("https://user.mihoyo.com")


        val handle = object : Handler() {
            override fun handleMessage(msg: Message) {
                msg.let {
//                    val msgtmp =  msg?.obj
                    val sharedPreference =  getSharedPreferences("COOKIE", Context.MODE_PRIVATE)
                    val string = sharedPreference.getString("body", "稍等。还没获取到")
                    val editText = findViewById<EditText>(R.id.input)
                    editText.setText(string)

                    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    //clipData中的this就是需要复制的文本
                    val clipData = ClipData.newPlainText("",string)
                    cm.setPrimaryClip(clipData)
                    Toast.makeText(this@MainActivity, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                }
            }
        }

        R.id.cookieBtn.onClick(this){

            val sharedPreference =  getSharedPreferences("COOKIE", Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            val instance = CookieManager.getInstance()
            val cookie = instance.getCookie("https://user.mihoyo.com")
            HttpUtil.getAuthkey(cookie,editor,handle)
        }

//        R.id.showcookie.onClick(this){
//            val sharedPreference =  getSharedPreferences("COOKIE", Context.MODE_PRIVATE)
//            val string = sharedPreference.getString("body", "稍等。还没获取到")
//            val editText = findViewById<EditText>(R.id.input)
//            editText.setText(string)
//        }
    }
}
fun Int.onClick(activity: Activity, click:()->Unit){
    activity.findViewById<View>(this).setOnClickListener{
        click()
    }
}
