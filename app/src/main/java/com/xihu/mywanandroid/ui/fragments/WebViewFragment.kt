package com.xihu.mywanandroid.ui.fragments


import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.navigation.Navigation
import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.net.beans.ConfigBean
import com.xihu.mywanandroid.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_web_view.*

class WebViewFragment : BaseFragment() {

    companion object {
        val WEBVIEW_SEARCH_URL = "com.xihu.mywanandroid.ui.fragments.WebViewFragment.URL"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webview.webViewClient = object :WebViewClient() {
            private var mWebView:WebView?=null
            init {
                mWebView = webview
            }
            override fun onRenderProcessGone(
                view: WebView?,
                detail: RenderProcessGoneDetail
            ): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!detail.didCrash()) {
                        mWebView?.also { webView ->
                            webviewCont.removeView(webView)
                            webView.destroy()
                            mWebView = null
                        }
                        return true
                    }
                }

                return false
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                println("url: $url")
                return false
            }

        }

        webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                loading.progress = newProgress
                loading.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
            }
        }

        webview.settings.apply {
            useWideViewPort = true
            loadWithOverviewMode = true
        }

        println("WebViewFragment: url ${arguments?.getString(WEBVIEW_SEARCH_URL)}")
        webview.loadUrl(arguments?.getString(WEBVIEW_SEARCH_URL))
    }

    override fun onBackForawrd() =
        if (webview != null && webview.canGoBack()) {
            webview.goBack()
            true
        } else {
            false
        }
}
