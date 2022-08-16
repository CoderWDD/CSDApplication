package com.example.common.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.example.common.R

fun Activity.setStatusBarColor(color: Int) {
    window.statusBarColor = color
}

fun Activity.hideSystemStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}
/**沉浸状态栏*/
fun Activity.immersionStatusBar() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    //设置专栏栏和导航栏的底色，透明
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        window.navigationBarDividerColor = Color.TRANSPARENT
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.white)
    }
    //设置沉浸后专栏栏和导航字体的颜色，
    ViewCompat.getWindowInsetsController(findViewById<FrameLayout>(android.R.id.content))?.let { controller ->
        //false 为 白色， true 为黑色
        controller.isAppearanceLightStatusBars = true
        controller.isAppearanceLightNavigationBars = true
    }
}