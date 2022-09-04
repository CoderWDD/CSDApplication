package com.example.article.data.entity.relationship.article_tags

import androidx.room.Junction
import androidx.room.Relation
import com.example.article.data.entity.Tag

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */

/**根据BaseArticle找到tags的一对多关系类
 * */
data class BaseArticle2Tags(
    val baseArticleID: Int,
    @Relation(
        parentColumn = "baseArticleID",
        entityColumn = "tagID",
        associateBy = Junction(BaseArticleTagCrossRef::class)
    )
    val tags: List<Tag>
)