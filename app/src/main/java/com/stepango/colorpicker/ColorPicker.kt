package com.stepango.colorpicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color.*
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
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
    val strokeSize = 2 * context.resources.displayMetrics.density
    val rainbowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    val rainbowBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = WHITE
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    val pickPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var pick = 0.5f
    var verticalGridSize = 0f
    var rainbowBaseline = 0f
    var showPreview = false
    var listener: OnColorChangedListener? = null

    override fun onDraw(canvas: Canvas) {
        drawPicker(canvas)
        drawColorAim(canvas, rainbowBaseline, verticalGridSize.toInt() / 2, verticalGridSize * 0.5f, color)
        if (showPreview) {
            drawColorAim(canvas, verticalGridSize, (verticalGridSize / 1.4f).toInt(), verticalGridSize * 0.7f, color)
        }
    }

    private fun drawPicker(canvas: Canvas) {
        val lineX = verticalGridSize / 2f
        val lineY = rainbowBaseline.toFloat()
        rainbowPaint.strokeWidth = verticalGridSize / 1.5f + strokeSize
        rainbowBackgroundPaint.strokeWidth = rainbowPaint.strokeWidth + strokeSize
        canvas.drawLine(lineX, lineY, width - lineX, lineY, rainbowBackgroundPaint)
        canvas.drawLine(lineX, lineY, width - lineX, lineY, rainbowPaint)
    }

    private fun drawColorAim(canvas: Canvas, baseLine: Float, offset: Int, size: Float, color: Int) {
        val circleCenterX = offset + pick * (canvas.width - offset * 2)
        canvas.drawCircle(circleCenterX, baseLine, size, pickPaint.apply { this.color = WHITE })
        canvas.drawCircle(circleCenterX, baseLine, size - strokeSize, pickPaint.apply { this.color = color })
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
        verticalGridSize = height / 3f
        rainbowPaint.shader = shader
        rainbowBaseline = verticalGridSize / 2f + verticalGridSize * 2
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
            listener?.onColorChanged(color)
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