package com.xihu.mywanandroid.ui.activities

import android.content.res.Resources.NotFoundException
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.ui.fragments.BaseFragment
import com.xihu.mywanandroid.ui.interfaces.FragmentBackListener
import com.xihu.mywanandroid.ui.jetpack.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var drawerlayout:DrawerLayout
    private lateinit var currentNavController: LiveData<NavController>
    private var backListener: FragmentBackListener?=null

    override fun layoutResID() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val navGraphIds = listOf(
                R.navigation.bottom_nav_home,
                R.navigation.bottom_nav_square,
                R.navigation.bottom_nav_public,
                R.navigation.bottom_nav_proj
            )

            val controller = bottom_navigation.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.main_nav_container,
                intent = intent
            )

            currentNavController = controller
        }

        try {
            println("ResourceName ${this.getResources().getResourceName(R.navigation.bottom_nav_proj)}")
        } catch (e: NotFoundException) {
        }

    }

    override fun initData() {
    }

    // 返回是在FragmentNavigator中处理
    override fun onNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    fun setBackForwardListener(listener: FragmentBackListener?) {
        this.backListener = listener
    }

    override fun onBackPressed() {
        if (backListener != null && backListener!!.onBackForawrd()) {
            return
        }
        super.onBackPressed()
    }
}
