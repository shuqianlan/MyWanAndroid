package com.xihu.mywanandroid.ui.fragments


import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.google.android.material.appbar.AppBarLayout
import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.databinding.FragmentHomeBinding
import com.xihu.mywanandroid.databinding.HomeArticleBinding
import com.xihu.mywanandroid.databinding.HomeBannerItemBinding
import com.xihu.mywanandroid.net.beans.Banner
import com.xihu.mywanandroid.net.beans.Article
import com.xihu.mywanandroid.ui.activities.BaseActivity
import com.xihu.mywanandroid.ui.activities.MainActivity
import com.xihu.mywanandroid.ui.adapters.BottomRefreshAdapter
import com.xihu.mywanandroid.ui.jetpack.viewmodels.HomeViewModel
import com.xihu.mywanandroid.ui.view.CarouselLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_fragment_home.*

class HomeFragment : BaseFragment<HomeViewModel>() {

    override fun providerViewModelClazz()=HomeViewModel::class.java
    var once = ObservableBoolean(true)

    private lateinit var adapter:BottomRefreshAdapter<Article,HomeArticleBinding>

    override fun initViewModel() {
        viewModel.topArticles.observe(this, Observer {
            refresh_layout.isRefreshing = false
            adapter.extendDatas(it)
        })

        viewModel.homeArticles.observe(this, Observer {
            adapter.extendDatas(it.datas)

            if (it.over) {
                adapter.setToEnd(true)
            }
        })

        viewModel.topBanners.observe(this, Observer {
            fillBanners(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_home, container, false)
        val binder = DataBindingUtil.bind<FragmentHomeBinding>(view)
        binder?.viewModel = viewModel
        binder?.lifecycleOwner = viewLifecycleOwner
        binder?.fragment = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 设置ToolBar
        (activity as BaseActivity)?.setSupportActionBar(toolbar)

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            val scrollHeight = Math.abs(app_bar.height - toolbar.height)
            val alpha = (Math.abs(offset)/1.0/scrollHeight*100).toInt()
            toolbar.alpha = if (alpha >= 60) (alpha-60)*0.025.toFloat() else 0f
        })

        articles.adapter = BottomRefreshAdapter.Builder<Article,HomeArticleBinding>(viewLayout = { _ -> R.layout.home_article}, clazz = Article::class.java, instance = object :BottomRefreshAdapter.InstanceBeansCallBack<Article,HomeArticleBinding> {
                override fun instance(adapter: BottomRefreshAdapter<Article,HomeArticleBinding>) =
                    object :SortedListAdapterCallback<Article>(adapter) {
                        override fun areItemsTheSame(item1: Article, item2: Article): Boolean {
                            return item1 == item2
                        }

                        override fun compare(o1: Article, o2: Article) =
                            if (o1.stick and o2.stick)
                                (o2.publishTime - o1.publishTime).toInt()
                            else if (o1.stick) {
                                -1
                            } else if (o2.stick) {
                                1
                            } else {
                                (o2.publishTime - o1.publishTime).toInt()
                            }

                        override fun areContentsTheSame(
                            oldItem: Article?,
                            newItem: Article?
                        ): Boolean {
                            return oldItem?.id == newItem?.id
                        }
                    }
            }, onLoadData = {viewModel.loadHomeArticles()})
        .bindView { binder, bean -> binder.article = bean }
        .callOnClick { _, bean -> showWebviewFragment(bean.link) }
        .build().also {
            adapter = it
        }

        refresh_layout.setOnRefreshListener {
            with(viewModel) {
                loadTopArticles()
            }
        }

        carousel_cont.setOnScrollListener(object :CarouselLayout.OnScrollListener {
            override fun callOnClick(index: Int, view: View) {
                showWebviewFragment((view.tag as Banner).url)
            }
        })

        up_to_top.setOnClickListener {
            articles.smoothScrollToPosition(0)
            up_to_top.scaleX = 0f
            up_to_top.scaleY = 0f
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_homes, menu)

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.search) {
//                (activity as MainActivity).bottom_navigation.visibility = View.GONE
                Navigation.findNavController(toolbar).navigate(R.id.action_navigation_home_to_searchFragment)
            }
            return@setOnMenuItemClickListener true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun fillBanners(banners:List<Banner>) {
        carousel_cont.removeAllViews()

        with(carousel_cont) {
            for (banner in banners) {
                val viewBinding = DataBindingUtil.inflate<HomeBannerItemBinding>(LayoutInflater.from(context), R.layout.home_banner_item, this, true)
                viewBinding.banner = banner
                viewBinding.bannerCont.tag = banner
            }
            autoPlay(true)
        }

    }

    override fun onRetryDatas(view: View) {
        viewModel.loadHomeItems()
    }

    private fun showWebviewFragment(url:String) {
        Navigation.findNavController(carousel_cont).navigate(R.id.action_navigation_home_to_webViewFragment, Bundle().also {
            it.putString(WebViewFragment.WEBVIEW_SEARCH_URL, url)
        })
    }
}
