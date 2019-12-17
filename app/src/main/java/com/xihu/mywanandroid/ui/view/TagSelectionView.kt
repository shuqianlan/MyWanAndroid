package com.xihu.mywanandroid.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xihu.mywanandroid.R

class TagSelectionView :ViewGroup {
    private val text_tags = listOf(
        "剑来", "雪中悍刀行", "飞升之后", "妖神记", "神墓", "完美世界", "将夜",
        "原始战记", "回到过去变成猫", "未来天王", "大师兄贼稳健", "蛮荒速成流"
    )

    private var tagListener:OnTagClickListener?=null
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        post {
            text_tags.forEachIndexed { _, s ->
                val view = LayoutInflater.from(context).inflate(R.layout.tag_selection_item, this, false)

                if (view is TextView) {
                    view.text = s
                    view.tag = s
                }
                addView(view)
            }

            requestLayout()
        }
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (childCount == 0) {
            width = 0
            heightSize = if (heightMode == MeasureSpec.EXACTLY) heightSize else 0
        } else {
            var sumChildsWidth = 0
            for (index in 0 until childCount) {
                val child = getChildAt(index)
                if (child.visibility == View.GONE) {
                    continue
                }

                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0) // 不考虑Margin
                sumChildsWidth += if(child.measuredWidth > width) width else child.measuredWidth // 含Margin
            }

            val rows = sumChildsWidth / width + if (sumChildsWidth % width == 0) 0 else 1
            val child = getChildAt(0)
            val lp = child.layoutParams as MarginLayoutParams
            heightSize = if (heightMode == MeasureSpec.EXACTLY) heightSize else rows * (child.measuredHeight + lp.topMargin + lp.bottomMargin)
        }
        setMeasuredDimension(width, heightSize)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) {
            return
        }

        var rows = 0
        var offsetX = 0
        for (index in 0 until childCount) {
            val child = getChildAt(index)

            if (child is TextView) {
                val childHeight = child.measuredHeight
                val lp = child.layoutParams as MarginLayoutParams
                val childWidth = if(child.measuredWidth > width) {
                    child.text = measureMaxWidth(child.text.toString())
                    width
                } else {
                    child.measuredWidth
                }

                if ((offsetX + childWidth) > width) {
                    offsetX = 0
                    rows++
                }
                val y = rows * childHeight + rows*lp.topMargin + if (rows > 0) rows*lp.bottomMargin else 0
                offsetX += lp.leftMargin
                child.layout(offsetX, y, offsetX + childWidth, y + childHeight)
                offsetX += childWidth + lp.rightMargin
            }

        }

    }

    private fun measureMaxWidth(text:String):String {
        var realWidth = width //- GapLT * 2

        var step = 0
        var child_measure_width = 0
        var title = text
        do {
            title = if (step != 0) text.substring(0, text.length-step) + "..." else text
            child_measure_width = textPaint.measureText(title, 0, title.length).toInt()
            step++
        } while (child_measure_width > realWidth)

        return title
    }

    // 保证自身已经绘制，在子View绘制之前.
    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)

//        val realWidth = width // - GapLT * 2
//        var sumWidth  = 0
//        var tempX = 0
//        text_tags.forEachIndexed { _, text ->
//            val matched_text = measureMaxWidth(text = text)
//
//            val currentWidth = if (matched_text.equals(text)) {
//                textPaint.measureText(matched_text).toInt() + GapRT
//            } else {
//                realWidth
//            }
//            sumWidth += currentWidth
//            val rows = kotlin.math.ceil(sumWidth.toDouble() / (1.0000001*realWidth)).toInt()
//            println("dispatchDraw rows: $rows $matched_text")
//            val positionY = (rows * GapRT + childViewHeight*(2*rows-1)/2)
//            val positionX = sumWidth % realWidth
//            canvas?.drawText(matched_text, positionX.toFloat(), positionY.toFloat(), textPaint)
//            sumWidth += currentWidth
//        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    interface OnTagClickListener {
        fun onTagClick(view:View)
    }

    fun setTagClickListener(listener: OnTagClickListener) {
        tagListener = listener
    }

    // 此处可以考虑AOP实现 AOP
    override fun addView(child: View?) {
        super.addView(child)
        child?.setOnClickListener {
            tagListener?.onTagClick(child)
        }
    }

    override fun addView(child: View?, width: Int, height: Int) {
        super.addView(child, width, height)

        child?.setOnClickListener {
            tagListener?.onTagClick(child)
        }
    }

    override fun addView(child: View?, params: LayoutParams?) {
        super.addView(child, params)

        child?.setOnClickListener {
            tagListener?.onTagClick(child)
        }
    }

    override fun addView(child: View?, index: Int) {
        super.addView(child, index)

        child?.setOnClickListener {
            tagListener?.onTagClick(child)
        }
    }

    override fun addView(child: View?, index: Int, params: LayoutParams?) {
        super.addView(child, index, params)

        child?.setOnClickListener {
            tagListener?.onTagClick(child)
        }
    }

}