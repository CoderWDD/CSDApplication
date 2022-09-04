package com.example.article.data.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * <pre>
 *     author : 叶梦璃愁 
 *     time   : 2022/08/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
/** 记录文章简短的信息：用于 更新 */
@Entity(tableName = "base_article")
data class BaseArticle(
    @PrimaryKey var baseArticleID: Int,
    val path: String,
    val updatedAt: String,
    override val title: String,
    var belongFolderID: Int = -1
): IFile {

    companion object{
        val differCallback = object : DiffUtil.ItemCallback<BaseArticle>(){
            override fun areItemsTheSame(oldItem: BaseArticle, newItem: BaseArticle): Boolean {
                return oldItem.baseArticleID == newItem.baseArticleID
            }

            override fun areContentsTheSame(oldItem: BaseArticle, newItem: BaseArticle): Boolean {
                return oldItem == newItem
            }
        }
    }
}