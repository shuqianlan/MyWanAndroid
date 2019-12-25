package com.xihu.mywanandroid.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.google.android.material.tabs.TabLayout
import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.databinding.FragmentProjBinding
import com.xihu.mywanandroid.databinding.HomeArticleBinding
import com.xihu.mywanandroid.databinding.LayoutProjectItemBinding
import com.xihu.mywanandroid.net.beans.Article
import com.xihu.mywanandroid.net.beans.Project
import com.xihu.mywanandroid.ui.adapters.BottomRefreshAdapter
import com.xihu.mywanandroid.ui.jetpack.viewmodels.ProjViewModel
import kotlinx.android.synthetic.main.content_fragment_home.*
import kotlinx.android.synthetic.main.content_fragment_proj.*
import kotlinx.android.synthetic.main.search_history_item.view.*

class ProjFragment : BaseViewModelFragment<ProjViewModel>() {

    override fun providerViewModelClazz() = ProjViewModel::class.java

    override fun initViewModel() {
        viewModel.projects.observe(this, Observer {
            println("Projectes $it")
            bindProjects(it.toMutableList())
        })

        viewModel.projectItems.observe(this, Observer {
            (list_projects.adapter as BottomRefreshAdapter<Article, LayoutProjectItemBinding>).extendDatas(it.datas)
            println("ProjectsItems: $it")
            println("isOver: ${it.over}")
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binder = FragmentProjBinding.inflate(layoutInflater, container, false)
        binder.fragment = this
        binder.viewModel = viewModel
        binder.lifecycleOwner = viewLifecycleOwner

        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {


                println("onTabReselected")
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                (list_projects.adapter as BottomRefreshAdapter<Article, LayoutProjectItemBinding>).clearDatas()
                (p0?.tag as? Project)?.also {
                    viewModel.resetLoadProjItems(it.id, it.id == -1)
                }
            }

        })

        list_projects.adapter = BottomRefreshAdapter.Builder<Article, LayoutProjectItemBinding>(
            clazz = Article::class.java, viewLayout = { _-> R.layout.layout_project_item}, onLoadData = {viewModel.loadProjItems()}, instance = object :BottomRefreshAdapter.InstanceBeansCallBack<Article,LayoutProjectItemBinding> {
                override fun instance(adapter: BottomRefreshAdapter<Article, LayoutProjectItemBinding>) =
                    object : SortedListAdapterCallback<Article>(adapter) {
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
            }
        )
        .bindView { binder, bean ->  binder.article = bean}
        .callOnClick { view, bean ->
            Navigation.findNavController(view).navigate(R.id.action_navigation_proj_to_webViewFragment2, Bundle().also {
                it.putString(WebViewFragment.WEBVIEW_SEARCH_URL, bean.link)
            })
        }
        .build()
    }

    private fun bindProjects(projects:MutableList<Project>) {

        projects.add(0, Project(name = getString(R.string.lastest_proj)))
        projects.sortedWith(Comparator { o1, o2 -> (o1.order - o2.order).toInt() })

        projects.forEachIndexed { _, project ->
            tab_layout.newTab().apply {
                text = project.name
                contentDescription = project.name
                tag = project
            }.also {
                tab_layout.addTab(it)
            }

        }

        tab_layout.getTabAt(0)?.also {
            it.select()
        }

    }
}
