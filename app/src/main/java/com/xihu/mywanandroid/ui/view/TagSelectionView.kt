package com.xihu.mywanandroid.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.WindowManager

class TagSelectionView :ViewGroup {
    private val text_tags = listOf("剑来", "雪中悍刀行", "飞升之后", "妖神记", "神墓", "完美世界", "将夜", "原始战记", "回到过去变成猫", "未来天王", "大师兄贼稳健", "蛮荒速成流")

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val VerticalGap = 10
    private val windWidth:Int

    init {
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).also {
            val point = Point()
            it.defaultDisplay.getRealSize(point)
            windWidth = point.x
        }
    }

    // 此处只需要测量高度即可.
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        var sumWidth = width
        var sumHeight = 0
        var tempWidth = 0

        for (tag in text_tags) {
            val childWidth = textPaint.measureText(tag).toInt()
            val fontMetrics = Paint.FontMetrics()
            textPaint.getFontMetrics(fontMetrics)
            val childHeight = (fontMetrics.descent - fontMetrics.top).toInt()
            println("childHeight $childHeight")

            tempWidth += if (childWidth > windWidth) width else childWidth
            if (tempWidth >= windWidth) {
                sumHeight += childHeight + VerticalGap
                tempWidth = 0
            }

        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    // 保证自身已经绘制，在子View绘制之前.
    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)


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

}