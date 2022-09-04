package com.example.article.data.mapper

import com.example.article.ui.base.timeTransform
import com.example.article.data.entity.Comment
import com.example.model.ArticleQuery

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class QueryData2CommentMapper: Mapper<ArticleQuery.List, Comment> {

    override suspend fun map(input: ArticleQuery.List): Comment {
        return Comment(
            commentID = input.id,
            content = input.content,
            createdAt = timeTransform(input.createdAt.toString()),
            updateAt = timeTransform(input.updatedAt.toString()),
            authorName = input.authorName,
            belongArticleID = 0
        )
    }
}