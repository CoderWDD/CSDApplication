package com.example.article.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.article.ui.inter.FragmentBackHandler

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/18
 *     desc   : 分发fragment的back事件
 *     version: 1.0
 * </pre>
 */
class BackHandlerHelper {

    companion object {
        /**
         * 将back事件分发给 FragmentManager 中管理的子Fragment，如果该 FragmentManager 中的所有Fragment都
         * 没有处理back事件，则尝试 FragmentManager.popBackStack()
         *
         * @return 如果处理了back键则返回 true
         */
        fun handleBackPress(fragmentManager: FragmentManager):Boolean{
            val fragments = fragmentManager.fragments
            if(fragments.isEmpty()) return false

            for(i in fragments.size - 1 downTo  0){
                val child = fragments[i]
                //判断是否为该fragment的返回事件
                if(isFragmentBackHandled(child)){
                    return true
                }
            }
            //后台堆栈实体个数
            if(fragmentManager.backStackEntryCount > 0){
                fragmentManager.popBackStack()
                return true
            }
            return false
        }

        /**
         * 将back事件分发给Fragment中的子Fragment,
         * 该方法调用了 {@link #handleBackPress(FragmentManager)}
         * @return 如果处理了back键则返回 true
         */
        fun handleBackPress(fragment: Fragment): Boolean{
            return handleBackPress(fragment.childFragmentManager)
        }

        /**
         * 将back事件分发给Activity中的子Fragment,
         * 该方法调用了 {@link #handleBackPress(FragmentManager)}
         * @return 如果处理了back键则返回 true
         */
        fun handleBackPress(fragmentActivity: FragmentActivity): Boolean {
            return handleBackPress(fragmentActivity.supportFragmentManager)
        }


        /**
         * 判断Fragment是否处理了Back键
         * @return 如果处理了back键则返回 true
         */
        private fun isFragmentBackHandled(fragment: Fragment?): Boolean{
            return  fragment != null
                    && fragment.isVisible
                    && fragment is FragmentBackHandler
                    && (fragment as FragmentBackHandler).onBackPressed()
        }
    }
}