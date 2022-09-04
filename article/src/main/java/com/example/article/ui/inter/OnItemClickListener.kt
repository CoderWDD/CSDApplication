package com.example.article.ui.inter

import androidx.recyclerview.widget.RecyclerView

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/31
 *     desc   : 点击监听接口
 *     version: 1.0
 * </pre>
 */

interface OnItemClickListener {

    fun onItemClick(holder: RecyclerView.ViewHolder, position: Int)

}