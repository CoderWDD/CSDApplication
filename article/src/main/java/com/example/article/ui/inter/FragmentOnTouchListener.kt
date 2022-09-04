package com.example.article.ui.inter

import android.view.GestureDetector
import android.view.MotionEvent

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface FragmentOnTouchListener {

    fun onTouch(event: MotionEvent?);
}


open class GestureListener(): GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {

    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

}