package com.example.article.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.article.R


/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class CommentAdapter(private val recyclerView: RecyclerView ): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private val data =  ArrayList<String>()


    init {
        data.addAll(listOf("1", "2", "3", "4", "5"))
    }

    /** 初始化数据  */
    fun initData(new: ArrayList<String>): Boolean{
        return if(data.isEmpty() && new.isNotEmpty()){
            data.addAll(new)
            notifyItemRangeRemoved(0, data.size)
            true
        } else {
            false
        }
    }

    /** 重新设置数据 */
    fun setData(new: ArrayList<String>): Boolean{
        return if(new.isNotEmpty()){
            data.clear()
            data.addAll(new)
            notifyItemRangeRemoved(0, data.size)
            true
        } else {
          false
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(recyclerView.context).inflate(R.layout.item_comment, parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        with(holder){
            tv_usename.text = "用户${position + 1}"
            tv_date.text = "$position 天前"
            tv_content.text = data[position]
        }
    }

    override fun getItemCount(): Int = data.size



    class CommentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val ibt_icon: ImageButton = itemView.findViewById(R.id.ibt_icon)
        val tv_usename: TextView = itemView.findViewById(R.id.tv_username)
        val tv_date: TextView = itemView.findViewById(R.id.tv_date)
        val tv_content: TextView = itemView.findViewById(R.id.tv_content)
    }
}