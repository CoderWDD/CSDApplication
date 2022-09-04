package com.example.article.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.example.article.data.entity.Tag
import com.example.article.data.entity.relationship.article_tags.Tag2BaseArticles

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Dao
interface TagDao {
    @Insert(onConflict = REPLACE)
    suspend fun inset( tags: List<Tag>)

    @Insert(onConflict = REPLACE)
    suspend fun inset(vararg tags: Tag)

    @Query("SELECT * FROM tag")
    suspend fun getAll(): List<Tag>

    @Query("DELETE  FROM tag")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM tag WHERE tagID = :tagID")
    suspend fun getTagWithBaseArticles(tagID: Int): Tag2BaseArticles?
}