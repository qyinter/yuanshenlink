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
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
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
//        myWebView.settings.userAgentString = "Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1"

//        myWebView.webViewClient = object : WebViewClient() {
//            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
//                request?.let {
//                    // 检查 URL 或者进行一些处理
//                    if (it.url.toString().contains("public-operation-hk4e.mihoyo.com")) {
//                        Log.d("WebViewRequest", "URL being loaded: ${it.url}")
//                        val editText = findViewById<EditText>(R.id.input)
//                        editText.setText(it.url.toString())
//                        val cm =
//                            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                        val clipData = ClipData.newPlainText("", it.url.toString())
//                        cm.setPrimaryClip(clipData)
//                        Toast.makeText(
//                            this@MainActivity,
//                            "已复制到剪贴板",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//                return super.shouldInterceptRequest(view, request)
//            }
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                url?.let {
//                    view?.loadUrl(it)
//                }
//                return true
//            }
//        }
        myWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // 注入 JavaScript 代码来修改 navigator.language
                view?.evaluateJavascript(
                    """
                            Object.defineProperty(navigator, 'language', {get: function(){return 'zh-CN';}});
                            Object.defineProperty(navigator, 'languages', {get: function(){return ['zh-CN'];}});
                          """, null
                )
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                request?.let {
                    if (it.url.toString().contains("public-operation-hk4e.mihoyo.com")) {
                        // 在主线程中执行 UI 操作
                        runOnUiThread {
                            Log.d("WebViewRequest", "URL being loaded: ${it.url}")
                            val editText = findViewById<EditText>(R.id.input)
                            editText.setText(it.url.toString())
                            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clipData = ClipData.newPlainText("", it.url.toString())
                            cm.setPrimaryClip(clipData)
                            Toast.makeText(
                                this@MainActivity,
                                "已复制到剪贴板",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                return super.shouldInterceptRequest(view, request)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    view?.loadUrl(it)
                }
                return true
            }
        }


        myWebView.loadUrl("https://ys.mihoyo.com/cloud/#/")

//        val handle: Handler = object : Handler(Looper.getMainLooper()) {
//            @SuppressLint("HandlerLeak")
//            override fun handleMessage(msg: Message) {
//                //正常操作
//                msg.let {
//                    val obj: ChouKaObj = msg.obj as ChouKaObj
//                    if (obj.code == 200) {
//                        val editText = findViewById<EditText>(R.id.input)
//                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
//
//                        if (obj.urlListObj.size > 1) {
//                            val array = Array(obj.urlListObj.size) { "" }
//                            for ((index, value) in obj.urlListObj.withIndex()) {
//                                array[index] = value.uid
//                            }
//                            builder.setIcon(R.drawable.ic_launcher_foreground)
//                                .setTitle("选择你想要查询的账号")
//                            builder.setItems(
//                                array
//                            ) { _, which ->
//                                for (listUrl in obj.urlListObj) {
//                                    if (listUrl.uid == array[which]) {
//                                        editText.setText(listUrl.url)
//                                        val cm =
//                                            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                                        //clipData中的this就是需要复制的文本
//                                        val clipData = ClipData.newPlainText("", listUrl.url)
//                                        cm.setPrimaryClip(clipData)
//                                        Toast.makeText(
//                                            this@MainActivity,
//                                            "已复制到剪贴板",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                }
//                                Toast.makeText(
//                                    applicationContext,
//                                    "你选择了Uid:" + array[which],
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                            val alert = builder.create()
//                            alert.show()
//                        } else {
//                            editText.setText(obj.urlListObj[0].url)
//                            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                            //clipData中的this就是需要复制的文本
//                            val clipData = ClipData.newPlainText("", obj.urlListObj[0].url)
//                            cm.setPrimaryClip(clipData)
//                            Toast.makeText(this@MainActivity, "已复制到剪贴板", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    } else {
//                        Toast.makeText(this@MainActivity, "请先登录米游社", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//            }
//        }

//        R.id.cookieBtn.onClick(this) {
//            val instance = CookieManager.getInstance()
//            val cookie = instance.getCookie("https://ys.mihoyo.com/cloud/#/")
//            HttpUtil.getAuthKey(cookie, handle)
//        }
    }
}

fun Int.onClick(activity: Activity, click: () -> Unit) {
    activity.findViewById<View>(this).setOnClickListener {
        click()
    }
}

