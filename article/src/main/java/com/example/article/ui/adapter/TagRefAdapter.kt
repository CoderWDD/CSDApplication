package com.example.article.ui.adapter;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.article.R
import com.example.article.data.entity.BaseArticle
import com.example.article.ui.base.BaseRecyclerAdapter

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/09/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */
 class TagRefAdapter(private val context: Context): BaseRecyclerAdapter<TagRefAdapter.TagRefViewHolder>(context) {

    private val data = mutableListOf<BaseArticle>()

    fun updateData(update: List<BaseArticle>){
        data.clear()
        data.addAll(update)
        notifyItemRangeChanged(0, data.size)
    }

    fun getData(position: Int) = data[position]

    override fun bindHolder(holder: TagRefViewHolder, position: Int) {
        holder.tv_title.text = data[position].title
        holder.tv_posiotn.text = "#${position + 1}"
        holder.tv_update.text = data[position].updatedAt
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagRefViewHolder {
        return TagRefViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recommend, parent, false))
    }

    override fun getItemCount(): Int  = data.size


    class TagRefViewHolder(item: View) : BaseViewHolder(item) {
        val tv_title: TextView = itemView.findViewById(R.id.tv_item_title)
        val tv_update: TextView = itemView.findViewById(R.id.tv_item_update_time)
        val tv_posiotn: TextView = itemView.findViewById(R.id.tv_item_position)
    }
}
