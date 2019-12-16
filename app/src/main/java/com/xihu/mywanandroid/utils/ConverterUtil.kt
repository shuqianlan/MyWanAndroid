package com.xihu.mywanandroid.utils

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide


object ConverterUtil {

    @BindingAdapter("isGone")
    @JvmStatic fun bindIsGone(@NonNull view: View, gone:Boolean) {
        view.visibility = if (gone) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @BindingAdapter("imageUrl")
    @JvmStatic fun loadImage(@NonNull imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }

}
