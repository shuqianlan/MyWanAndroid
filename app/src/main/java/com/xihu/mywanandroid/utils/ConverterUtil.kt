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

    @BindingAdapter("app:isGone")
    @JvmStatic fun bindIsGone(view: View, gone:Boolean) {
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

}
