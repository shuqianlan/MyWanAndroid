package com.xihu.mywanandroid.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xihu.mywanandroid.R

class SearchResultFragment : Fragment() {

    companion object {
        val SEARCHKEY = "com.xihu.mywanandroid.ui.fragments.SearchResultFragment.SEARCH_KEY"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("searchKey: ${arguments?.getString(SEARCHKEY)}")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }


}
