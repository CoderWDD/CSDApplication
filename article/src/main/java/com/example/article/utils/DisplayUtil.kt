package com.example.article.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.TypedValue

object DisplayUtil {
    /**dip 转 px */
    fun dip2px(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }

    internal fun sp2px(context: Context, sp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    /**根据手机的分辨率 px 转 dx*/
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
    /**根据手机的分辨率 dp 转 px*/
    fun dp2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun bitmapResize(src: Bitmap, pxX: Float, pxY: Float): Bitmap {
        //压缩图片
        val matrix = Matrix()
        matrix.postScale(pxX / src.width, pxY / src.height)
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    fun getScreenWidth(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.heightPixels
    }
}