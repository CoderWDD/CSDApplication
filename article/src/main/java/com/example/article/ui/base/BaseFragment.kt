package com.example.article.ui.base

import androidx.fragment.app.Fragment
import com.example.article.ui.inter.FragmentBackHandler
import com.example.article.utils.BackHandlerHelper

/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
open class BaseFragment: Fragment(), FragmentBackHandler {
    //处理返回事件
    override fun onBackPressed(): Boolean {
        return BackHandlerHelper.handleBackPress(this)
    }

}