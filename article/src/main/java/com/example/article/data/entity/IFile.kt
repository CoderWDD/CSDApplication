package com.example.article.data.entity

import androidx.recyclerview.widget.DiffUtil

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IFile{
    val title: String
    companion object{
        val differCallback = object : DiffUtil.ItemCallback<IFile>(){
            override fun areItemsTheSame(oldItem: IFile, newItem: IFile): Boolean {
                return if(oldItem is BaseArticle && newItem is BaseArticle){
                    oldItem.baseArticleID == newItem.baseArticleID
                }else if(oldItem is Folder && newItem is Folder){
                    oldItem.folderID == newItem.folderID
                }else false
            }

            override fun areContentsTheSame(oldItem: IFile, newItem: IFile): Boolean {
                return if(oldItem is BaseArticle && newItem is BaseArticle){
                    oldItem == newItem
                }else if(oldItem is Folder && newItem is Folder){
                    oldItem == newItem
                }else false
            }

        }
    }
}