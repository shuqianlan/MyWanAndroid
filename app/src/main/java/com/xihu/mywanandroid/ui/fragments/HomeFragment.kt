package com.xihu.mywanandroid.ui.fragments


import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.net.beans.Article
import com.xihu.mywanandroid.ui.adapters.BottomRefreshAdapter
import com.xihu.mywanandroid.ui.jetpack.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var viewModel:HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider.NewInstanceFactory().create(HomeViewModel::class.java)

        print("ahahahahahhahaha")
        viewModel.topArticles.observe(activity!!, Observer {
            print("====================>")
        })
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articles.adapter = BottomRefreshAdapter.Builder<String>(bindToView = { view, bean -> {
            if (view is TextView) {
                view.text = bean
            }
        }}, viewLayout = {viewType -> android.R.layout.simple_list_item_1}, onLoadData = { adapter -> { runBlocking {
                delay(2000)
                print("loading more ")
            }
        } },
        clazz = String::class.java, instance = object :BottomRefreshAdapter.InstanceBeansCallBack<String> {
                override fun instance(adapter: BottomRefreshAdapter<String>) =
                    object :SortedListAdapterCallback<String>(adapter) {
                        override fun areItemsTheSame(item1: String?, item2: String?): Boolean {
                            return item1!! == item2!!
                        }

                        override fun compare(o1: String?, o2: String?): Int {
                            return o1!!.compareTo(o2!!)
                        }

                        override fun areContentsTheSame(
                            oldItem: String?,
                            newItem: String?
                        ): Boolean {
                            return oldItem!!.equals(newItem)
                        }

                    }
            })
        .build()

        refresh_layout.setOnRefreshListener {
            refresh_layout.postDelayed(Runnable {
                refresh_layout.isRefreshing = false
            }, 3000)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_homes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}
