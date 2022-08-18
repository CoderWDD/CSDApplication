package com.example.article.viewModel

import androidx.lifecycle.ViewModel
import com.example.article.constants.Constants

/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class PathViewModel: ViewModel() {

    //存放每个pathFragment的绝对路径
    private val pathList = ArrayList<ArrayList<String>>().apply {
        add(arrayListOf("移动开发"))
        add(arrayListOf("WEB开发"))
        add(arrayListOf("游戏开发"))
        add(arrayListOf("人工智能"))
    }

    /** 获得路径 */
    fun getPath(tag: Int): ArrayList<String> {
        return if(tag < Constants.Android || tag > Constants.AI) {
            emptyList<String>() as ArrayList<String>
        }
        else pathList[tag]
    }
    /** 添加路径 */
    fun addPath(tag: Int, path: String){
        if (tag < Constants.Android || tag > Constants.AI) throw IndexOutOfBoundsException("tag is invalid")

        pathList[tag].add(path)
    }
    /** 删除最后一级路径 */
    fun removePath(tag: Int){
        if (tag < Constants.Android || tag > Constants.AI) throw IndexOutOfBoundsException("tag is invalid")

        pathList[tag].apply {
            if(size > 1) {
                removeAt(size - 1)
            }else throw IndexOutOfBoundsException("the root path cannot remove")
        }
    }


}