package com.example.article.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.article.R
import com.example.article.ui.base.timeTransform
import com.example.article.ui.base.usernameToIcon
import com.example.article.data.entity.Comment


/**
 *     author : 叶梦璃愁
 *     time   : 2022/08/14
 *     desc   :
 *     version: 1.0
 */
class CommentAdapter(private val recyclerView: RecyclerView ): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private val data =  ArrayList<Comment>()

    /** 重新设置数据 */
    fun updateData(new: List<Comment>){
        data.clear()
        data.addAll(new)
        notifyItemRangeRemoved(0, data.size)
    }

    fun hasComments() = data.isNotEmpty()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(recyclerView.context).inflate(R.layout.item_comment, parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        with(holder){
            tv_icon.text = usernameToIcon(data[position].authorName)
            tv_usename.text = data[position].authorName
            tv_date.text = data[position].createdAt
            tv_content.text = data[position].content
        }
    }

    override fun getItemCount(): Int = data.size


    class CommentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val tv_icon: TextView = itemView.findViewById(R.id.tv_item_icon)
        val tv_usename: TextView = itemView.findViewById(R.id.tv_item_username)
        val tv_date: TextView = itemView.findViewById(R.id.tv_item_date)
        val tv_content: TextView = itemView.findViewById(R.id.tv_item_content)

    }
}