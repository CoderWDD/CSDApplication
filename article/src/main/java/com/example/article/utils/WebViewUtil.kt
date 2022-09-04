package com.example.article.utils

import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
object WebViewUtil {
    /** 设置WebView */
    fun setWebView(webView: WebView){
        if (Build.VERSION.SDK_INT > 21) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }
        webView.settings.javaScriptEnabled = true
        //不阻塞网络图片的加载
        webView.settings.blockNetworkImage = false
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.loadWithOverviewMode = false
        webView.settings.setSupportZoom(true)
        webView.settings.useWideViewPort = true

       webView.settings.defaultTextEncodingName = "UTF-8"
    }

}