package com.example.article.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.article.R
import com.example.article.data.entity.Tag
import com.example.article.ui.fragment.TagRefFragment
import com.example.article.ui.inter.OnItemClickListener

/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class TagAdapter(
    private val context: Context
): RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    private val tags = mutableListOf<Tag>()

    fun updateData(newData: List<Tag>){
        tags.clear()
        tags.addAll(newData)
        notifyItemRangeChanged(0, tags.size)
    }

    fun clearData(){
        tags.clear()
    }

    fun getData(position: Int) = tags[position]

    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }


    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.tv_tag.text = tags[position].tag

        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder(LayoutInflater.from(context).inflate(R.layout.item_tag, parent, false))
    }

    override fun getItemCount(): Int = tags.size

    class TagViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val tv_tag: TextView = item.findViewById(R.id.tv_item_tag)
    }


}