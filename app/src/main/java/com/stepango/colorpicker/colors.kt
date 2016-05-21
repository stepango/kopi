package com.stepango.colorpicker

import android.graphics.Color
import android.graphics.Color.*
import android.support.annotation.FloatRange
import java.lang.Math.round

/**
 * Created by stepangoncarov on 21/05/16.
 */
fun interpColor(@FloatRange(from = 0.0, to = 1.0) unit: Float, colors: IntArray): Int {
    if (unit <= 0) {
        return colors[0];
    }
    if (unit >= 1) {
        return colors[colors.size - 1];
    }

    var p = unit * (colors.size - 1);

    val i = p.toInt();
    // take fractional part
    p -= i;

    val c0 = colors[i];
    val c1 = colors[i + 1];
    // Calculates each channel separately
    val a = avg(alpha(c0), alpha(c1), p);
    val r = avg(red(c0), red(c1), p);
    val g = avg(green(c0), green(c1), p);
    val b = avg(blue(c0), blue(c1), p);

    return Color.argb(a, r, g, b);
}

/**
 * Calculates int value in between two integers using percentage
 * @param s - start
 * @param e - end
 * @param p - scaled percentage
 */
fun avg(s: Int, e: Int, @FloatRange(from = 0.0, to = 1.0) p: Float) = s + round(p * (e - s))