package com.example.article.data.local

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
import com.example.article.data.entity.BaseArticle
import com.example.article.data.entity.relationship.article_tags.BaseArticle2Tags

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Dao
@RewriteQueriesToDropUnusedColumns
interface BaseArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg articles: BaseArticle)

    @Update
    fun update(vararg articles: BaseArticle)

    @Delete
    fun delete(vararg articles: BaseArticle)

    @Query("DELETE  FROM base_article WHERE baseArticleID = :articleID")
    fun deleteAccordingId(articleID: Int)

    @Query("DELETE FROM base_article")
    fun deleteAll()

    @Query("SELECT * FROM base_article WHERE baseArticleID = :articleID")
    fun getBaseArticle(articleID: Int): BaseArticle?

    @Query("SELECT * FROM base_article WHERE path LIKE :folderPath ||'%'")
    fun getBaseArticle(folderPath: String): List<BaseArticle>

    @Query("SELECT * FROM base_article WHERE title LIKE '%'|| :query || '%'")
    fun getBaseArticleByQuery(query: String): PagingSource<Int, BaseArticle>

    @Transaction
    @Query("SELECT * FROM base_article WHERE baseArticleID = :baseArticleID")
    suspend fun getBaseArticleWithTags(baseArticleID: Int): BaseArticle2Tags?



    @Query("SELECT title FROM base_article")
    suspend fun getAllBaseArticleTitles(): List<String>
}