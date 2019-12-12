package com.xihu.mywanandroid.ui.activities

import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.ui.jetpack.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private lateinit var drawerlayout:DrawerLayout
    private lateinit var currentNavController: LiveData<NavController>

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

    }

    override fun initData() {
    }

    override fun onNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}
