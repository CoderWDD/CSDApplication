package com.example.article.data.entity.relationship.article_tags

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.article.data.entity.BaseArticle
import com.example.article.data.entity.Tag

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
data class Tag2BaseArticles(
    @Embedded val tag: Tag,
    @Relation(
        parentColumn = "tagID",
        entityColumn = "baseArticleID",
        associateBy = Junction(BaseArticleTagCrossRef::class)
    )
    val baseArticles: List<BaseArticle>
)