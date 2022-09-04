package com.example.article.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.article.R
import com.example.article.data.entity.BaseArticle
import com.example.article.ui.base.BaseRecyclerAdapter
import com.example.article.ui.inter.OnItemClickListener
import com.example.article.ui.inter.OnItemLongClickListener

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ResultAdapter(private val context: Context)
    :PagingDataAdapter<BaseArticle, ResultAdapter.ResultViewHolder>(BaseArticle.differCallback){

    fun getBaseArticle(position: Int) = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        return ResultViewHolder(LayoutInflater.from(context).inflate(R.layout.item_path,  parent, false))
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val data = getItem(position)
        if(data is BaseArticle){
            holder.iv_icon.setBackgroundResource(R.drawable.ic_file)
        }
        holder.tv_title.text = data?.title

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

    class ResultViewHolder(item: View): BaseRecyclerAdapter.BaseViewHolder(item) {
        val iv_icon = item.findViewById<ImageView>(R.id.iv_path_icon)
        val tv_title = item.findViewById<TextView>(R.id.tv_item_file_title)
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    /**外部设置监听*/
    fun  setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }
    /**外部设置监听*/
    fun  setOnItemLongClickListener(listener: OnItemLongClickListener){
        mOnItemLongClickListener = listener
    }

}