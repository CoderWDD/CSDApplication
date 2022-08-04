package com.example.common.utils

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsets

fun Activity.setStatusBarColor(color: Int) {
    window.statusBarColor = color
}

fun Activity.hideSystemStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
    }else {
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}