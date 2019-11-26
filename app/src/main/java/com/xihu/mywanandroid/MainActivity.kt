package com.xihu.mywanandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.xihu.mywanandroid.databinding.ActivityMainBinding
import com.xihu.mywanandroid.ui.jetpack.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var drawerlayout:DrawerLayout
    private lateinit var currentNavController: LiveData<NavController>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerlayout = binding.drawerlayout

        if (savedInstanceState == null) {
            print("savedInstanceState null")
            setupBottomNavigationView()
        }
    }

    private fun setupBottomNavigationView() {

        // 其中的id和menu中的id保持一致，会在setAction时设置标题
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

    override fun onNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}
