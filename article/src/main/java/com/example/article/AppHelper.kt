package com.example.article

import android.content.Context

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
object AppHelper {
    lateinit var mContext: Context

    fun init(context: Context){
        this.mContext = context
    }
}