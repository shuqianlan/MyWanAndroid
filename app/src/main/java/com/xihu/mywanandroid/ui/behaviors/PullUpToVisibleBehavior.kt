package com.xihu.mywanandroid.ui.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PullUpToVisibleBehavior : CoordinatorLayout.Behavior<FloatingActionButton> {

    private enum class Status {
        READYING, LOADING, ENDING
    }
    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    private var state = Status.READYING

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        return dependency is RecyclerView
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        // 表示接收垂直滚动事件
        return (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

        if (dy < 0) {
            state = if (state == Status.READYING) Status.LOADING else state

            if (state == Status.LOADING) {
                state = Status.ENDING
                child.scaleX = 0f
                child.scaleY = 0f
                child.animate().setDuration(50).scaleX(1f).scaleY(1f).start()
            }
        } else {
            state = Status.READYING
            if (child.animation != null && !child.animation.hasEnded()) {
                child.animation.cancel()
            }
            child.scaleX = 0f
            child.scaleY = 0f
        }
    }
}