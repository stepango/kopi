package com.stepango.colorpicker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by stepangoncarov on 08/06/16.
 */
class RainbowTextView @JvmOverloads constructor(
        ctx: Context,
        attrs: AttributeSet? = null,
        gefStyleAttr: Int = 0
) : TextView(ctx, attrs, gefStyleAttr) {

    var shader: Shader? = null
    val shaderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight.toFloat()
        val width = measuredWidth.toFloat()
        val shader = LinearGradient(
                0f, 0f, width, height,
                intArrayOf(Color.RED, Color.BLUE, Color.GREEN),
                null,
                Shader.TileMode.CLAMP
        )
        this.shader = shader
        shaderPaint.shader = shader
    }

    override fun onDraw(canvas: Canvas?) {
        paint.setShadowLayer(8f, 0f, 0f, currentTextColor)
        super.onDraw(canvas)
        canvas?.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), shaderPaint)
        paint.clearShadowLayer()
        super.onDraw(canvas)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }
}
