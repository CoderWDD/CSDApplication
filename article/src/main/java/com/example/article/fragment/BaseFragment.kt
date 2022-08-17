package com.example.article.fragment

import androidx.fragment.app.Fragment
import com.example.article.utils.BackHandlerHelper
import com.example.article.utils.FragmentBackHandler

/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
open class BaseFragment: Fragment(), FragmentBackHandler{
    //处理返回事件
    override fun onBackPressed(): Boolean {
        return BackHandlerHelper.handleBackPress(this)
    }

}