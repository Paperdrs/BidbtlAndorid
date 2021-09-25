package com.bidbatl.dileveryapp

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.bidbatl.dileveryapp.databinding.ActivityWebViewBinding
import com.bidbatl.dileveryapp.util.Common

class WebViewActivity : AppCompatActivity() {
lateinit var webViewBinding: ActivityWebViewBinding
    private lateinit var pageUrl: String
    private lateinit var pageTitle: String
    lateinit var toolbar : Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webViewBinding = DataBindingUtil.setContentView(this,R.layout.activity_web_view)
        pageUrl = intent.getStringExtra(Common.PreferenceKey.pageUrl)
            ?: throw IllegalStateException("field $pageUrl missing in Intent")
        pageTitle = intent.getStringExtra(Common.PreferenceKey.pageTitle)
            ?: throw IllegalStateException("field $pageTitle missing in Intent")
        setToolBar()
        initWebView()
        setWebClient()
        loadUrl(pageUrl)
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webViewBinding.webview.settings.javaScriptEnabled = true
        webViewBinding.webview.settings.loadWithOverviewMode = true
        webViewBinding.webview.settings.useWideViewPort = true
        webViewBinding.webview.settings.domStorageEnabled = true
        webViewBinding.webview.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }
    }
    private fun setToolBar(){
        toolbar = findViewById(R.id.toolbar_normal)
        val backAction = toolbar.findViewById<ImageView>(R.id.imageView_back)
        val title = toolbar.findViewById<TextView>(R.id.textView_title)
        title.text = pageTitle
        backAction.setOnClickListener {
            finish()
        }
        setSupportActionBar(toolbar)
    }
    private fun setWebClient() {
        webViewBinding.webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                webViewBinding.progressBar.progress = newProgress
                if (newProgress < 100 && webViewBinding.progressBar.visibility == ProgressBar.GONE) {
                    webViewBinding.progressBar.visibility = ProgressBar.VISIBLE
                }
                if (newProgress == 100) {
                    webViewBinding.progressBar.visibility = ProgressBar.GONE
                }
            }
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK && webViewBinding.webview.canGoBack()) {
            webViewBinding.webview.goBack()
            return true
        }
        // If it wasn't the Back key or there's no web page history, exit the activity)
        return super.onKeyDown(keyCode, event)
    }
    private fun loadUrl(pageUrl: String) {
        webViewBinding.webview.loadUrl(pageUrl)
    }
}
