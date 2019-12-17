package com.xihu.mywanandroid.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
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

    fun View.dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        ).toInt()
    }

    fun View.sp2px(spVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal, context.resources.displayMetrics
        ).toInt()
    }

    fun View.px2dp(pxVal: Float): Float {
        val scale = context.resources.displayMetrics.density
        return pxVal / scale
    }

    fun View.px2sp(pxVal: Int): Float {
        return pxVal / context.resources.displayMetrics.scaledDensity
    }
}
