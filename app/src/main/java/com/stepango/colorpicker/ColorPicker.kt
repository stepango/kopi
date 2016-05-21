package com.stepango.colorpicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Color.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

@SuppressWarnings("MagicNumber")
class ColorPicker @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val colors = intArrayOf(RED, GREEN, BLUE)
    val rainbowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    val pickPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var pick = 0.5f
    var path = Path()
    var verticalGridSize: Int = 0
    var rainbowBaseline: Int = 0
    var showPreview: Boolean = false
    var listener: OnColorChangedListener? = null

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, rainbowPaint)
        val color = color
        drawColorAim(canvas, rainbowBaseline, verticalGridSize / 2, verticalGridSize * 0.5f, color)
        if (showPreview) {
            drawColorAim(canvas, verticalGridSize, (verticalGridSize / 1.4f).toInt(), verticalGridSize * 0.7f, color)
        }
    }

    private fun drawColorAim(canvas: Canvas, baseLine: Int, offset: Int, size: Float, color: Int) {
        pickPaint.color = Color.WHITE
        canvas.drawCircle(offset + pick * (canvas.width - offset * 2), baseLine.toFloat(), size, pickPaint)
        pickPaint.color = color
        canvas.drawCircle(offset + pick * (canvas.width - offset * 2), baseLine.toFloat(), size * 0.9f, pickPaint)
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight
        val width = measuredWidth
        val shader = LinearGradient(
                height / 4.0f,
                height / 2.0f,
                width - height / 4.0f,
                height / 2.0f,
                colors,
                null,
                Shader.TileMode.CLAMP)
        verticalGridSize = height / 3
        rainbowPaint.shader = shader
        rainbowPaint.strokeWidth = verticalGridSize / 1.5f

        // central grid is for rainbow

        val lineX = verticalGridSize / 2
        rainbowBaseline = verticalGridSize / 2 + verticalGridSize * 2
        path.moveTo(lineX.toFloat(), rainbowBaseline.toFloat())
        path.lineTo((width - lineX).toFloat(), rainbowBaseline.toFloat())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN) {
            pick = event.x / measuredWidth.toFloat()
            if (pick < 0) {
                pick = 0f
            } else if (pick > 1) {
                pick = 1f
            }
            if (listener != null) {
                listener!!.onColorChanged(color)
            }
            showPreview = true
        } else if (action == MotionEvent.ACTION_UP) {
            showPreview = false
        }
        postInvalidateOnAnimation()
        return true
    }

    val color: Int
        get() = interpColor(pick, colors)

    fun setOnColorChangedListener(listener: OnColorChangedListener) {
        this.listener = listener
    }

    interface OnColorChangedListener {
        fun onColorChanged(color: Int)
    }

}