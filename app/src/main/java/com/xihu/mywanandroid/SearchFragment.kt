package com.xihu.mywanandroid


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.xihu.mywanandroid.databinding.BottomrefreshlayoutBinding
import com.xihu.mywanandroid.databinding.FragmentSearchBinding
import com.xihu.mywanandroid.databinding.SearchHistoryItemBinding
import com.xihu.mywanandroid.ui.adapters.BottomRefreshAdapter
import com.xihu.mywanandroid.ui.adapters.ViewHolder
import com.xihu.mywanandroid.ui.view.TagSelectionView
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSearchBinding.inflate(layoutInflater).root
    }

    private val history_keys = mutableListOf("java", "Kotlin", "Dart")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tagSelectionView.setTagClickListener(object :TagSelectionView.OnTagClickListener {
            override fun onTagClick(view: View) {
                Snackbar.make(view, view.tag as String, Snackbar.LENGTH_SHORT).show()
            }
        })

        search_history.adapter = object :RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(SearchHistoryItemBinding.inflate(layoutInflater, parent, false))
            }

            override fun getItemCount(): Int {
                return history_keys.size
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                (holder.bindr as SearchHistoryItemBinding).key = history_keys[position]
                holder.bindr.clear.setOnClickListener {
                    history_keys.removeAt(position)
                    notifyItemRemoved(position)
                    notifyDataSetChanged()
                }

                holder.bindr.cont.setOnClickListener {
                    Snackbar.make(holder.bindr.root, history_keys[position], Snackbar.LENGTH_SHORT).show()
                }
            }

        }
    }

    fun onNavigationUp(view:View) {
        Navigation.findNavController(navbar_back).navigateUp()
    }
}
