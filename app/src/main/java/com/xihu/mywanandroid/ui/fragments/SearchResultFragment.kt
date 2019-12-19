package com.xihu.mywanandroid.ui.fragments


import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.databinding.FragmentSearchResultBinding
import com.xihu.mywanandroid.databinding.HomeArticleBinding
import com.xihu.mywanandroid.net.beans.Article
import com.xihu.mywanandroid.ui.adapters.BottomRefreshAdapter
import com.xihu.mywanandroid.ui.jetpack.viewmodels.SearchResultViewModel
import kotlinx.android.synthetic.main.content_fragment_home.*
import kotlinx.android.synthetic.main.content_fragment_search_result.*
import kotlinx.android.synthetic.main.content_fragment_search_result.toolbar
import kotlinx.android.synthetic.main.home_article.view.*

class SearchResultFragment : BaseFragment<SearchResultViewModel>() {

    companion object {
        val SEARCHKEY = "com.xihu.mywanandroid.ui.fragments.SearchResultFragment.SEARCH_KEY"
    }

    override fun providerViewModelClazz() = SearchResultViewModel::class.java

    private lateinit var keyWord:String
    private lateinit var adapter:BottomRefreshAdapter<Article, HomeArticleBinding>

    override fun initViewModel() {
        viewModel.searchResults.observe(this, Observer {
            println("searchResults : $it, size:${it.datas.size}")

            val datas = it.datas.asSequence().map {article ->
                article.shareUser?.trim()
                article.author?.trim()
                article
            }.toList()

            adapter.setToEnd(it.over)
            adapter.extendDatas(datas)
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSearchResultBinding.inflate(layoutInflater, container, false).also {
            it.fragment = this
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner // 若ViewBinding使用的是ViewModel，则必须设置lifecyclleOwner
        }.root

    }

    override fun onStart() {
        super.onStart()

        val cache = context?.getSharedPreferences(SEARCHKEY, Context.MODE_PRIVATE)?.getString(SEARCHKEY, "") ?: ""
        keyWord = arguments?.getString(SEARCHKEY) ?: cache

        if (!keyWord.equals(cache)) {
            context?.getSharedPreferences(SEARCHKEY, Context.MODE_PRIVATE)?.edit()?.putString(
                SEARCHKEY, keyWord)?.commit()
        }

        println("keyword: $keyWord")
        toolbar.title = Html.fromHtml("<em class='highlight'>Kotlin协程</em>它不香吗？").toString()

        viewModel.initialize(0, keyWord)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        search_results.adapter = BottomRefreshAdapter.Builder<Article,HomeArticleBinding>(
            viewLayout = { _ -> R.layout.home_article}, clazz = Article::class.java, instance = object :BottomRefreshAdapter.InstanceBeansCallBack<Article,HomeArticleBinding> {
                override fun instance(adapter: BottomRefreshAdapter<Article,HomeArticleBinding>) =
                    object : SortedListAdapterCallback<Article>(adapter) {
                        override fun areItemsTheSame(item1: Article, item2: Article): Boolean {
                            return item1 == item2
                        }

                        override fun compare(o1: Article, o2: Article) =
                            (o1.publishTime - o2.publishTime).toInt()

                        override fun areContentsTheSame(
                            oldItem: Article?,
                            newItem: Article?
                        ): Boolean {
                            return oldItem?.id == newItem?.id
                        }
                    }
            }, onLoadData = {
                viewModel.search(keyWord)
            }
        )
        .bindView { binder, bean ->
            binder.article = bean
        }
        .callOnClick { view, bean ->
            Navigation.findNavController(view).navigate(R.id.action_searchResultFragment_to_webViewFragment, Bundle().apply {
                putString(WebViewFragment.WEBVIEW_SEARCH_URL, bean.link)
            })
        }
        .build()
        .also { adapter = it }

        toolbar.setNavigationOnClickListener {
            Navigation.findNavController(toolbar).navigateUp()
        }
    }

    override fun onRetryDatas(view: View) {
        viewModel.initialize(0, keyWord)
    }
}
