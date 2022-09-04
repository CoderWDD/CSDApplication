package com.example.article.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
import com.example.article.data.entity.Article

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
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg articles: Article)

    @Update
    suspend fun update(vararg articles: Article)

    @Delete
    suspend fun delete(vararg articles: Article)

    @Query("DELETE FROM article")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM article WHERE articleID = :articleID")
    suspend fun getArticle(articleID: Int): Article?

    @Transaction
    @Query("SELECT * FROM article WHERE path LIKE :folderPath ||'%'")
    suspend fun getArticles(folderPath: String): List<Article>

//    @Transaction
//    @Query("SELECT * FROM article WHERE articleID = :articleID")
//    suspend fun getArticleTags(articleID: Int): List<Article2Tags>
//
//    @Transaction
//    @Query("SELECT * FROM tag WHERE tagID = :tagID ")
//    suspend fun getTag2Articles(tagID: Int): List<Tag2Articles>
}
