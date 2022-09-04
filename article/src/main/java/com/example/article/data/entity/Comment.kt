package com.example.article.data.entity

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
@Entity
data class
Comment(
    @PrimaryKey val commentID: Int,
    val belongArticleID: Int, //用于指明该评论属于哪一篇article
    val authorName: String,
    val createdAt: String,
    val updateAt: String,
    val content: String
)