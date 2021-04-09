package com.kahin.espressodemo.ui.main.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.kahin.espressodemo.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            wv.apply {

                clearCache(true)

                val webSettings = this.settings
                // use js
                webSettings.javaScriptEnabled = true

                webSettings.setSupportZoom(true)
                webSettings.builtInZoomControls = true

                webChromeClient = WebChromeClient()

                loadUrl("https://bing.com")

            }

            wv.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progressBar.visibility = View.VISIBLE
                }
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.wv.destroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (binding.wv.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            binding.wv.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}