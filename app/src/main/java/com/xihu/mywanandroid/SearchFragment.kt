package com.xihu.mywanandroid


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.xihu.mywanandroid.databinding.FragmentSearchBinding
import com.xihu.mywanandroid.ui.view.TagSelectionView
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSearchBinding.inflate(layoutInflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tagSelectionView.setTagClickListener(object :TagSelectionView.OnTagClickListener {
            override fun onTagClick(view: View) {
                Snackbar.make(view, view.tag as String, Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    public fun onNavigationUp(view:View) {
        Navigation.findNavController(navbar_back).navigateUp()
    }
}
