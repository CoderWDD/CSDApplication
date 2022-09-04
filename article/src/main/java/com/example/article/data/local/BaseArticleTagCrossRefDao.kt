package com.example.article.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.article.data.entity.relationship.article_tags.BaseArticleTagCrossRef

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Dao
interface BaseArticleTagCrossRefDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg refs: BaseArticleTagCrossRef)

    @Query("SELECT * FROM base_tags_ref WHERE baseArticleID = :baseArticleID")
    suspend fun getBaseArticleWithTags(baseArticleID: Int): BaseArticleTagCrossRef?
}