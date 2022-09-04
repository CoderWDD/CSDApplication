package com.example.article.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.article.data.entity.Comment


@Dao
interface  CommentDao {
    @Insert
    suspend fun inserts(comments: List<Comment>)


    @Insert
    suspend fun insert(comment: Comment)

    @Query("SELECT *FROM comment WHERE belongArticleID = :articleID")
    suspend fun getCommentsByArticleID(articleID: Int): List<Comment>
}
