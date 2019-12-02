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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.topArticles.observe(viewLifecycleOwner, Observer {
            print("HomeFragment viewLifeCycleOwner $it ")
            print("HomeFragment BBBBBBBBB ${articles.adapter is BottomRefreshAdapter<*>}")
            
            (articles.adapter as BottomRefreshAdapter<Article>).beans?.addAll(it)
        })

        viewModel.topArticles.observe(activity!!, Observer {
            print("activity!! $it")
        })

        viewModel.topArticles.observe(this, Observer {
            print("fragment!! $it")
        })

        articles.adapter = BottomRefreshAdapter.Builder<Article>(bindToView = { view, bean -> {
            if (view is TextView) {
                view.text = bean.title
            }
        }}, viewLayout = {viewType -> android.R.layout.simple_list_item_1}, onLoadData = { adapter -> { runBlocking {
                delay(2000)
                viewModel.loadHomeArticles()
                print("loading more ")
            }
        } },
        clazz = Article::class.java, instance = object :BottomRefreshAdapter.InstanceBeansCallBack<Article> {
                override fun instance(adapter: BottomRefreshAdapter<Article>) =
                    object :SortedListAdapterCallback<Article>(adapter) {
                        override fun areItemsTheSame(item1: Article?, item2: Article?): Boolean {
                            return false
                        }

                        override fun compare(o1: Article?, o2: Article?): Int {
                            return 1
                        }

                        override fun areContentsTheSame(
                            oldItem: Article?,
                            newItem: Article?
                        ): Boolean {
                            return false
                        }

                    }
            })
        .build()

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
