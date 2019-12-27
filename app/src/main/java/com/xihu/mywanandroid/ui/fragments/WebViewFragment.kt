package com.xihu.mywanandroid.ui.fragments


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.xihu.mywanandroid.databinding.FragmentWebViewBinding
import com.xihu.mywanandroid.ui.jetpack.viewmodels.WebviewModel
import kotlinx.android.synthetic.main.content_fragment_webview.*

class WebViewFragment : BaseViewModelFragment<WebviewModel>() {

    companion object {
        val WEBVIEW_SEARCH_URL = "com.xihu.mywanandroid.ui.fragments.WebViewFragment.URL"
    }

    private lateinit var binder:FragmentWebViewBinding
    override fun providerViewModelClazz()=WebviewModel::class.java

    override fun initViewModel() {
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentWebViewBinding.inflate(layoutInflater, container, false).apply {
            viewmodel = viewModel
            fragment = this@WebViewFragment
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    private var url:String? = null

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
                url = request?.url.toString()
                return false
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)

                viewModel.isOnError(true)
            }

        }

        webview.webChromeClient = object : WebChromeClient() {
            var once = true
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                loading?.apply {
                    progress = newProgress
                    visibility = if (newProgress == 100) View.GONE else View.VISIBLE

                    if (once and (visibility == View.VISIBLE)) {
                        viewModel.isOnError(false)
                        once = false
                    }
                    once = newProgress == 100
                }
            }

        }

        webview.settings.apply {
            useWideViewPort = true
            loadWithOverviewMode = true
        }

        url = arguments?.getString(WEBVIEW_SEARCH_URL)

        loadUrl()
    }

    private fun loadUrl() {
        url?.also {
            webview.loadUrl(it)
        }
    }

    override fun onRetryDatas(view:View) {
        loadUrl()
    }

    override fun onBackForawrd() =
        if (webview != null && webview.canGoBack()) {
            webview.goBack()
            true
        } else {
            false
        }
}
