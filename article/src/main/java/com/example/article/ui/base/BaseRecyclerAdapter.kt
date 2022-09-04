package com.example.article.ui.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/18
 *     desc   : 封装点击效果
 *     version: 1.0
 * </pre>
 */
abstract class BaseRecyclerAdapter<VH: BaseRecyclerAdapter.BaseViewHolder>(private val context: Context): RecyclerView.Adapter<VH>() {

    private var mOnItemClickListener: OnItemClickListener<VH>? = null

    private var mOnItemLongClickListener: OnItemLongClickListener<VH>? = null

    /** 点击监听接口 */
    interface OnItemClickListener<VH: BaseViewHolder> {
        fun onItemClick(holder: VH, position: Int)
    }

    /**长按监听接口*/
    interface OnItemLongClickListener<VH> {
        fun onItemLongClick(holder: VH, position: Int)
    }
    /**外部设置监听*/
    fun  setOnItemClickListener(listener: OnItemClickListener<VH>) {
        mOnItemClickListener = listener
    }
    /**外部设置监听*/
    fun  setOnItemLongClickListener(listener: OnItemLongClickListener<VH>){
        mOnItemLongClickListener = listener
    }

    /**数据绑定ViewHolder*/
    abstract fun bindHolder(holder: VH, position: Int)

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.item.apply {
            setOnClickListener {
                mOnItemClickListener?.onItemClick(holder, position)
            }
            setOnLongClickListener {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener?.onItemLongClick(holder, position)
                    return@setOnLongClickListener true
                }
                return@setOnLongClickListener false
            }
        }
        bindHolder(holder, position)
    }

    open class BaseViewHolder(val item: View) : RecyclerView.ViewHolder(item){}
}