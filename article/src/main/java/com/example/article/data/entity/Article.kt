package com.example.article.data.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.example.article.constants.Constants
import com.example.article.data.local.CommentListConverter
import com.example.article.data.local.TagListConverter

/**
 *     author : 叶梦璃愁
 *     time   : 2022/08/25
 *     desc   : 文章Entity
 *     version: 1.0
 */
/**文章的详细信息*/
@Entity
data class Article(

    @PrimaryKey val articleID: Int,
    val path: String,
    val title: String,
    val description: String,
    val content: String,
    val render: String?,
    val createdAt: String,
    val updatedAt: String,
    val contentType: String,
    val authorID: Int,
    val authorName: String,
    val authorEmail: String,
    val creatorName: String,
    //article和 tag是多对多的关系
//    var tags: List<Tag>,
    //article 和 comment 是 一对多的关系
//    var comments: List<Comment>,
    var belongFolderID: Int = Constants.UNINITIALIZED
){
    var tags = mutableListOf<Tag>()

    var comments = mutableListOf<Comment>()

    companion object{
        val differCallback = object : DiffUtil.ItemCallback<Article>(){
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.articleID == newItem.articleID
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

}


