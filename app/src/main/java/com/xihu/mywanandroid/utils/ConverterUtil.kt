package com.xihu.mywanandroid.utils

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide


object ConverterUtil {

    val TAG = "ConverterUtil"
    // app:isGone=@{false}
    @BindingAdapter("app:isGone")
    @JvmStatic fun bindIsGone(view: View, gone:Boolean) {
        if (view is CoordinatorLayout) {
            println("\n$TAG CoordinatorLayout $gone")
        } else if (view is TextView) {
            println("\n$TAG TextView $gone")
        } else if (view is LottieAnimationView) {
            println("\n$TAG LottieAnimationView $gone")
        } else if (view is ProgressBar) {
            println("\n$TAG ProgressBar $gone")
        } else {
            println("\n$TAG ${view.javaClass} $gone")
        }

        view.visibility = if (gone) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @BindingAdapter("app:imageUrl")
    @JvmStatic fun loadImage(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }

    @BindingConversion
    @JvmStatic fun booleanToVisibility(vis:Boolean):Int {
        return if (vis) View.VISIBLE else View.GONE
    }
}
