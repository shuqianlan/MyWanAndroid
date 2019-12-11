package com.xihu.mywanandroid.ui.fragments


import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.google.android.material.appbar.AppBarLayout
import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.databinding.FragmentHomeBinding
import com.xihu.mywanandroid.databinding.HomeArticleBinding
import com.xihu.mywanandroid.databinding.HomeBannerItemBinding
import com.xihu.mywanandroid.net.beans.Banner
import com.xihu.mywanandroid.net.beans.TopArticle
import com.xihu.mywanandroid.ui.activities.BaseActivity
import com.xihu.mywanandroid.ui.adapters.BottomRefreshAdapter
import com.xihu.mywanandroid.ui.jetpack.viewmodels.HomeViewModel
import com.xihu.mywanandroid.ui.view.CarouselLayout
import kotlinx.android.synthetic.main.content_fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeViewModel>() {

    override fun providerViewModelClazz()=HomeViewModel::class.java
    var once = ObservableBoolean(true)

    private lateinit var adapter:BottomRefreshAdapter<TopArticle,HomeArticleBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.topArticles.observe(this, Observer {
            refresh_layout.isRefreshing = false
            println("topArticles_observ $it")
            adapter.extendDatas(it)
        })

        viewModel.homeArticles.observe(this, Observer {
            adapter.extendDatas(it.datas)

            println("homeArticles_observ $it")
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

        articles.adapter = BottomRefreshAdapter.Builder<TopArticle,HomeArticleBinding>(viewLayout = { _ -> R.layout.home_article}, clazz = TopArticle::class.java, instance = object :BottomRefreshAdapter.InstanceBeansCallBack<TopArticle,HomeArticleBinding> {
                override fun instance(adapter: BottomRefreshAdapter<TopArticle,HomeArticleBinding>) =
                    object :SortedListAdapterCallback<TopArticle>(adapter) {
                        override fun areItemsTheSame(item1: TopArticle, item2: TopArticle): Boolean {
                            return item1 == item2
                        }

                        override fun compare(o1: TopArticle, o2: TopArticle) =
                            if (o1.fresh and o2.fresh)
                                (o2.publishTime - o1.publishTime).toInt()
                            else if (o1.fresh) {
                                -1
                            } else if (o2.fresh) {
                                1
                            } else {
                                (o2.publishTime - o1.publishTime).toInt()
                            }

                        override fun areContentsTheSame(
                            oldItem: TopArticle?,
                            newItem: TopArticle?
                        ): Boolean {
                            return oldItem?.id == newItem?.id
                        }
                    }
            }, onLoadData = {viewModel.loadHomeArticles()})
        .bindView { binder, bean -> binder.article = bean }
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
                val tag = (view as? ConstraintLayout)?.tag
                println("callOnClick tag $tag ")
            }
        })

        up_to_top.setOnClickListener {
            articles.smoothScrollToPosition(0)
            up_to_top.scaleX = 0f
            up_to_top.scaleY = 0f
        }

//        home_root_cont.setOnClickListener {
//            println("why not here")
//            onRetryDatas(it)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_homes, menu)
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

    fun onRetryDatas(view:View):Unit {
        println("\n ======================== onRetryDatas ")
        viewModel.loadHomeItems()
    }

}
