package com.example.article.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.article.AppHelper
import com.example.article.ui.inter.FragmentOnTouchListener
import com.example.article.data.local.ArticleDataBase
import com.example.article.utils.LocalStorageUtil

/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
open class BaseActivity: AppCompatActivity() {

    private val onTouchListeners: ArrayList<FragmentOnTouchListener> = ArrayList(10)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        for (listener in onTouchListeners) {
            listener.onTouch(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun registerFragmentListener(listener: FragmentOnTouchListener) {
        onTouchListeners.add(listener)
    }

    fun unregisterMyOnTouchListener(listener: FragmentOnTouchListener) {
        onTouchListeners.remove(listener);
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    override fun onStart() {
        super.onStart()
        LocalStorageUtil.dataBase = Room.databaseBuilder(applicationContext, ArticleDataBase::class.java, "article_database").build()

        //监听网络变化
//        registerNetworkStatus()
    }

    override fun onPause() {
        super.onPause()
//        unRegisterNetworkStatus()
    }



}

