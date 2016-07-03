package com.stepango.colorpicker

import android.content.Context

/**
 * Created by stepangoncarov on 05/06/16.
 */

fun Int.dpToPx(context: Context) = (context.resources.displayMetrics.density * this).toFloat()