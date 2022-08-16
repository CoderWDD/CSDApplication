package com.example.article.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout


/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/14
 *     desc   : 获取、设置控件信息
 *     version: 1.0
 * </pre>
 */
object WidgetController {
    /**
     * 获取控件宽
     */
    fun getWidth(view: View): Int {
        val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(w, h)
        return view.measuredWidth
    }

    /**
     * 获取控件高
     */
    fun getHeight(view: View): Int {
        val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(w, h)
        return view.measuredHeight
    }

    fun getWidthAndHeight(view: View):Pair<Int, Int>{
        val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(w, h)
        return Pair(view.measuredWidth, view.measuredHeight)
    }

    fun getScreenWidthAndHeight(activity: Activity): Pair<Int, Int>{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = activity.windowManager.currentWindowMetrics.bounds
            Pair(bounds.width(), bounds.height())
        } else {
            val point = Point()
            activity.windowManager.defaultDisplay.getSize(point)
            Pair(point.x, point.y)
        }
    }


    /**
     * 设置控件所在的位置X，并且不改变宽高，
     * X为绝对位置，此时Y可能归0
     */
    fun setLayoutX(view: View, x: Int) {
        val margin = MarginLayoutParams(view.layoutParams)
        margin.setMargins(x, margin.topMargin, x + margin.width, margin.bottomMargin)
        val layoutParams = RelativeLayout.LayoutParams(margin)
        view.layoutParams = layoutParams
    }

    /**
     * 设置控件所在的位置Y，并且不改变宽高，
     * Y为绝对位置，此时X可能归0
     */
    fun setLayoutY(view: View, y: Int) {
        val margin = MarginLayoutParams(view.layoutParams)
        margin.setMargins(margin.leftMargin, y, margin.rightMargin, y + margin.height)
        val layoutParams = RelativeLayout.LayoutParams(margin)
        view.layoutParams = layoutParams
    }

    /**
     * 设置控件所在的位置YY，并且不改变宽高，
     * XY为绝对位置
     */
    fun  setLayout(view: View, x: Int, y: Int, type: String) {
        val margin = MarginLayoutParams(view.layoutParams)
        margin.setMargins(x, y, x + margin.width, y + margin.height)
        val layoutParams =
            when(type) {
                "Linear"-> LinearLayout.LayoutParams(margin)
                "Relative"-> RelativeLayout.LayoutParams(margin)
                "Frame"-> FrameLayout.LayoutParams(margin)
                else-> ViewGroup.LayoutParams(margin)
            }
        view.layoutParams = layoutParams
        view.requestLayout()
    }
}