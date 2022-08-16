package com.example.article.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.article.R

import java.util.*
import kotlin.collections.ArrayList

/**
 *     author : swk
 *     time   : 2022/08/16
 *     desc   : 显示文件路径的adapter
 *     version: 1.0
 */
class PathAdapter(private val recyclerView: RecyclerView) : RecyclerView.Adapter<PathAdapter.PathViewHolder>() {

    //设置点击事件
    interface OnItemClickListener {
        fun onItemClick(holder: PathViewHolder, position: Int)
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    //文件路径
    private val filePath = ArrayList<String>()

    private val pathStack = Stack<ArrayList<String>>()

    //TODO:移出该行
    init {
        filePath.addAll(arrayListOf("1", "2", "3", "4"))
    }


    fun updateFilePath(new: ArrayList<String>) {
        filePath.clear()
        filePath.addAll(new)
        notifyItemRangeChanged(0, filePath.size)
    }

    /**文件路径的更新:下一级目录*/
    fun change(){
        pathStack.push(
            ArrayList<String>().apply {
                addAll(filePath)
            })
        updateFilePath(new = arrayListOf("a", "b", "c", "d"))

    }
    /**文件路径的更新：上一级目录*/
    fun preChange(){
        if(pathStack.isNotEmpty()) {
            updateFilePath(pathStack.pop())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PathViewHolder {
        return PathViewHolder(LayoutInflater.from(recyclerView.context).inflate(R.layout.item_path, parent, false), mOnItemClickListener!!)
    }

    override fun onBindViewHolder(holder: PathViewHolder, position: Int) {
        holder.apply {
            setOnItemClickListener(mOnItemClickListener!!)
            tv_path.text = filePath[position]
        }
    }

    override fun getItemCount(): Int = filePath.size


    class PathViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val tv_path: TextView = itemView.findViewById(R.id.tv_path)
        val iv_icon: ImageView = itemView.findViewById(R.id.iv_path_icon)

        private lateinit var listener: OnItemClickListener

        constructor(itemView: View, listener: OnItemClickListener) : this(itemView) {
            itemView.setOnClickListener(this)
            this.listener = listener
        }

        override fun onClick(v: View?) {
            listener.onItemClick(this, adapterPosition)
        }

    }
}