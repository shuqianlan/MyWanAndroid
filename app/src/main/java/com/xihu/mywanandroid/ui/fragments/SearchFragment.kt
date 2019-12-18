package com.xihu.mywanandroid.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.databinding.FragmentSearchBinding
import com.xihu.mywanandroid.databinding.SearchHistoryItemBinding
import com.xihu.mywanandroid.net.beans.Hotkey
import com.xihu.mywanandroid.net.beans.SearchKey
import com.xihu.mywanandroid.ui.activities.MainActivity
import com.xihu.mywanandroid.ui.adapters.ViewHolder
import com.xihu.mywanandroid.ui.jetpack.viewmodels.SearchViewModel
import com.xihu.mywanandroid.ui.view.TagSelectionView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment<SearchViewModel>() {

    override fun providerViewModelClazz()=SearchViewModel::class.java
    private val beans= mutableListOf<SearchKey>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.hotkeys.observe(this, Observer {
            tagSelectionView.setTags(it.map { hotkey ->
                hotkey.name
            }, it)
        })

        viewModel.searchKeys.observe(this, Observer {

            beans.addAll(it)
            search_history.adapter?.notifyDataSetChanged()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSearchBinding.inflate(layoutInflater).also {
            it.searchFragment = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tagSelectionView.setTagClickListener(object :TagSelectionView.OnTagClickListener {
            override fun onTagClick(view: View) {
                search_value.setText((view.tag as Hotkey).name)
                onSearchKey(view)
            }
        })

        search_history.adapter = object :RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(SearchHistoryItemBinding.inflate(layoutInflater, parent, false))
            }

            override fun getItemCount(): Int {
                return beans.size
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                (holder.bindr as SearchHistoryItemBinding).key = beans[position].value
                holder.bindr.clear.setOnClickListener {
                    beans.removeAt(position)
                    notifyItemRemoved(position)
                    notifyDataSetChanged()
                }

                holder.bindr.cont.setOnClickListener {
                    Snackbar.make(holder.bindr.root, beans[position].value, Snackbar.LENGTH_SHORT).show()
                }
            }

        }

    }

    fun onNavigationUp(view:View) {
        (activity as MainActivity).bottom_navigation.visibility = View.VISIBLE
        Navigation.findNavController(navbar_back).navigateUp()
    }

    fun onSearchKey(view:View) {
        val searchKey = search_value.text.toString()
        println("searchKey: $searchKey")

        Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_searchResultFragment, Bundle().also {
            it.putString(SearchResultFragment.SEARCHKEY, searchKey)
        })
    }
}
