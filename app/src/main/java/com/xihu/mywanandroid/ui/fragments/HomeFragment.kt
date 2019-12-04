package com.xihu.mywanandroid.ui.fragments


import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.xihu.huidefeng.net.repository.RemoteRepository
import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.databinding.HomeArticleBinding
import com.xihu.mywanandroid.net.beans.Article
import com.xihu.mywanandroid.ui.adapters.BottomRefreshAdapter
import com.xihu.mywanandroid.ui.jetpack.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class HomeFragment : BaseFragment<HomeViewModel>() {

    override fun providerViewModelClazz()=HomeViewModel::class.java

    private lateinit var adapter:BottomRefreshAdapter<Article,HomeArticleBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.topArticles.observe(this, Observer {
            println("\ntopArticles fragment!! $it")
            adapter.extendDatas(it)
        })

        viewModel.homeArticles.observe(this, Observer {
            println("\nhomeArticles fragment!! $it")
            adapter.extendDatas(it)
        })

        lifecycle.addObserver(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
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

        //TODO:ViewDataBinding
        articles.adapter = BottomRefreshAdapter.Builder<Article,HomeArticleBinding>(viewLayout = {viewType -> R.layout.home_article}, onLoadData = { adapter -> { runBlocking {
                delay(2000)
                viewModel.loadHomeArticles()
                print("loading more ")
            }
        } },
        clazz = Article::class.java, instance = object :BottomRefreshAdapter.InstanceBeansCallBack<Article,HomeArticleBinding> {
                override fun instance(adapter: BottomRefreshAdapter<Article,HomeArticleBinding>) =
                    object :SortedListAdapterCallback<Article>(adapter) {
                        override fun areItemsTheSame(item1: Article, item2: Article): Boolean {
                            return item1 == item2
                        }

                        override fun compare(o1: Article?, o2: Article?): Int {
                            return ((o1?.publishTime ?: 0) - (o2?.publishTime ?: 0)).toInt()
                        }

                        override fun areContentsTheSame(
                            oldItem: Article?,
                            newItem: Article?
                        ): Boolean {
                            return oldItem?.title == newItem?.title
                        }

                    }
            })
        .build().also {
                adapter = it as BottomRefreshAdapter<Article,HomeArticleBinding>
        }

        refresh_layout.setOnRefreshListener {
            refresh_layout.postDelayed(Runnable {
                viewModel.launchUI {
                    println("refresh_layout start")
//                    val response = RemoteRepository.instance.topArticles()
//                    println("refresh_layout $response end")
                }
                refresh_layout.isRefreshing = false
            }, 3000)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_homes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}
