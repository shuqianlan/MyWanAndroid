package com.xihu.mywanandroid.ui.fragments


import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.databinding.HomeArticleBinding
import com.xihu.mywanandroid.databinding.HomeBannerItemBinding
import com.xihu.mywanandroid.net.beans.Banner
import com.xihu.mywanandroid.net.beans.TopArticle
import com.xihu.mywanandroid.ui.activities.BaseActivity
import com.xihu.mywanandroid.ui.adapters.BottomRefreshAdapter
import com.xihu.mywanandroid.ui.jetpack.viewmodels.HomeViewModel
import com.xihu.mywanandroid.ui.view.CarouselLayout
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeViewModel>() {

    override fun providerViewModelClazz()=HomeViewModel::class.java

    private lateinit var adapter:BottomRefreshAdapter<TopArticle,HomeArticleBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as BaseActivity)?.setSupportActionBar(toolbar)

        carousel_cont.isNestedScrollingEnabled = false
        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
            val scrollHeight = Math.abs(app_bar.height - toolbar.height)
            val alpha = (Math.abs(offset)/1.0/scrollHeight*100).toInt()
            toolbar.alpha = if (alpha >= 60) (alpha-60)*0.025.toFloat() else 0f
        })

        articles.adapter = BottomRefreshAdapter.Builder<TopArticle,HomeArticleBinding>(viewLayout = { viewType -> R.layout.home_article}, clazz = TopArticle::class.java, instance = object :BottomRefreshAdapter.InstanceBeansCallBack<TopArticle,HomeArticleBinding> {
                override fun instance(adapter: BottomRefreshAdapter<TopArticle,HomeArticleBinding>) =
                    object :SortedListAdapterCallback<TopArticle>(adapter) {
                        override fun areItemsTheSame(item1: TopArticle, item2: TopArticle): Boolean {
                            return item1 == item2
                        }

                        override fun compare(o1: TopArticle, o2: TopArticle): Int {
                            if (o1.fresh and o2.fresh)
                                return (o2.publishTime - o1.publishTime).toInt()
                            else if (o1.fresh) {
                                return -1
                            } else if (o2.fresh) {
                                return 1
                            }

                            return (o2.publishTime - o1.publishTime).toInt()
                        }

                        override fun areContentsTheSame(
                            oldItem: TopArticle?,
                            newItem: TopArticle?
                        ): Boolean {
                            return oldItem?.id == newItem?.id
                        }
                    }
            }, onLoadData = {viewModel.loadHomeArticles()})
        .build().also {
            adapter = it
        }

        refresh_layout.setOnRefreshListener {
            viewModel.loadTopArticles()
        }

        carousel_cont.setOnScrollListener(object :CarouselLayout.OnScrollListener {
            override fun callOnClick(index: Int, view: View) {
                val tag = view.tag as Banner
                println("callOnClick tag $tag")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_homes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun fillBanners(banners:List<Banner>) {
        carousel_cont.removeAllViews()

        for (index in 0 until banners.size) {
            val viewBinding = DataBindingUtil.inflate<HomeBannerItemBinding>(LayoutInflater.from(context), R.layout.home_banner_item, carousel_cont, true)
            viewBinding.banner = banners[index]
            viewBinding.root.tag = banners[index]
            Glide.with(this).load(banners[index].imagePath).into(viewBinding.bannerImg)
        }

        carousel_cont.autoPlay(true)
    }
}
