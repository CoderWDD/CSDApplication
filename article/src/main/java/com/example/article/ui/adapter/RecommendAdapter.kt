package com.example.article.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.article.R
import com.example.article.data.entity.Article
import com.example.article.data.entity.BaseArticle
import com.example.article.data.entity.IFile
import com.example.article.ui.base.BaseRecyclerAdapter
import com.example.article.data.remote.Response
import org.w3c.dom.Text

/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class RecommendAdapter(private val context: Context)
    : PagingDataAdapter<BaseArticle, RecommendAdapter.RecommendViewHolder>(BaseArticle.differCallback) {

    private var mOnItemClickListener: OnItemClickListener? = null

    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    /** 点击监听接口 */
    interface OnItemClickListener {
        fun onItemClick(holder: RecyclerView.ViewHolder, position: Int)
    }

    /**长按监听接口*/
    interface OnItemLongClickListener {
        fun onItemLongClick(holder: RecyclerView.ViewHolder, position: Int)
    }
    /**外部设置监听*/
    fun  setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }
    /**外部设置监听*/
    fun  setOnItemLongClickListener(listener: OnItemLongClickListener){
        mOnItemLongClickListener = listener
    }

    fun getBaseArticle(position: Int) = getItem(position)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        return RecommendViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_recommend,  parent, false)
        )
    }
    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        val data = getItem(position)
        holder.tv_title.text = data?.title
        holder.tv_update.text = data?.updatedAt
        holder.tv_posiotn.text = "#${position + 1}"
        //实现点击和长按
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
    }

    class RecommendViewHolder(itemView: View): BaseRecyclerAdapter.BaseViewHolder(itemView)  {
        val tv_title: TextView = itemView.findViewById(R.id.tv_item_title)
        val tv_update: TextView = itemView.findViewById(R.id.tv_item_update_time)
        val tv_posiotn: TextView = itemView.findViewById(R.id.tv_item_position)
    }
}

