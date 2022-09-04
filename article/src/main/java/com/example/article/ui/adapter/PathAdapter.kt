package com.example.article.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.article.R
import com.example.article.ui.base.BaseRecyclerAdapter
import com.example.article.data.entity.Article
import com.example.article.data.entity.BaseArticle
import com.example.article.data.entity.Folder
import com.example.article.data.entity.IFile

/**
 *     author : swk
 *     time   : 2022/08/16
 *     desc   : 显示文件路径的adapter
 *     version: 1.0
 */
class PathAdapter(private val context: Context) : BaseRecyclerAdapter<PathAdapter.PathViewHolder>(context) {

    private val FOLDER = 1

    private val ARTILE = 2

    //文件夹结点
    private var curFolder: Folder? = null

    //所有文件
    private var files = mutableListOf<IFile>()

    //更新文件
    fun updateFilePath(child: Folder?) {
        curFolder = child
        files.clear()
        curFolder?.let { join(it) }
        notifyDataSetChanged()
    }

    fun getFile(position: Int) = files[position]

    /**
     * 将folderNode中的文件集合为一个list
     * */
    private fun join(folderNode: Folder){
        for(folder in folderNode.childFolders.folders){
            files.add(folder)
        }
        for(baseArticle in folderNode.baseArticles.baseArticles){
            files.add(baseArticle)
        }
    }
    /**
     * 根据files中的position返回对应的folderId
     * */
    fun folderFromFiles(position: Int): Int{
        return curFolder?.childFolders?.folders?.find {
            (files[position] as Folder).folderID == it.folderID
        }?.folderID ?: -1
    }
    /**
     * 根据files中的position返回对应的 baseID
     * */
    fun baseArticleFromFiles(position: Int): BaseArticle?{
        return curFolder?.baseArticles?.baseArticles?.find {
            (files[position] as BaseArticle).baseArticleID == it.baseArticleID
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PathViewHolder {
        return PathViewHolder(LayoutInflater.from(context).inflate(R.layout.item_path, parent, false))
    }

    override fun bindHolder(holder: PathViewHolder, position: Int) {
        if (files[position] is Folder){
            holder.iv_icon.setBackgroundResource(R.drawable.ic_file_folder)
        }else {
            holder.iv_icon.setBackgroundResource(R.drawable.ic_file)
        }
        holder.tv_title.text = files[position].title
    }

    override fun getItemViewType(position: Int): Int {
        return if(files[position] is Folder) FOLDER else ARTILE
    }

    override fun getItemCount(): Int = files.size

    class PathViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val tv_title: TextView = itemView.findViewById(R.id.tv_item_file_title)
        val iv_icon: ImageView = itemView.findViewById(R.id.iv_path_icon)

    }


}