package com.example.article.data.entity.relationship.article_tags

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/28
 *     desc   : article和tag的关联表
 *     version: 1.0
 * </pre>
 */
@Entity(
    tableName = "base_tags_ref",
    primaryKeys = ["tagID", "baseArticleID"],
)
data class BaseArticleTagCrossRef(
    val tagID: Int,
    @ColumnInfo(index = true)
    val baseArticleID: Int
)