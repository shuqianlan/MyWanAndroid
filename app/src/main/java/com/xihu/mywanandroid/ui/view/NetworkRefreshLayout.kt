package com.xihu.mywanandroid.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.TypedArrayUtils.getResourceId
import com.xihu.mywanandroid.R

class NetworkRefreshLayout :ConstraintLayout {

    private var mainContResID = 0
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.also {
            context?.obtainStyledAttributes(attrs, R.styleable.NetworkRefreshLayout).also {
                mainContResID = it!!.getResourceId(R.styleable.NetworkRefreshLayout_content_main, mainContResID)

                it!!.recycle()
            }

            if (mainContResID != 0) {
                LayoutInflater.from(context).inflate(mainContResID, this, false).also {
                    this.addView(it, 0)
                }

            }
        }
    }

    private fun addProgressBar() {
        ProgressBar(context).also {
            val lp = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//            lp.marginStart = this

        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return super.generateDefaultLayoutParams()
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return super.generateLayoutParams(attrs)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
        return super.generateLayoutParams(p)
    }

}