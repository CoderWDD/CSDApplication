package com.example.article.data.mapper

import com.example.article.ui.base.removeEscapeSymbol
import com.example.article.ui.base.renderToHtml
import com.example.article.ui.base.timeTransform
import com.example.article.data.entity.Article
import com.example.article.data.entity.Comment
import com.example.article.data.entity.Tag
import com.example.article.utils.LocalStorageUtil
import com.example.model.ArticleQuery

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class QueryData2ArticleMapper: Mapper<ArticleQuery.Data, Article> {
    /**
     * 转换为Article
     * */
    companion object{
        private val tagMapper = QueryData2TagMapper()
        private val commentMapper = QueryData2CommentMapper()
        private val articleDao = LocalStorageUtil.dataBase.ArticleDao()
    }

    override suspend fun map(input: ArticleQuery.Data): Article {
        val tags = ArrayList<Tag>().apply {
            input.pages?.single?.tags?.forEach {
                it?.let {
                    add(tagMapper.map(it))
                }
            }
        }
        val comments = ArrayList<Comment>().apply {
            input.comments?.list?.forEach {
                it?.let {
                    add(commentMapper.map(it))
                }
            }
        }
        return Article(
            articleID = input.pages?.single!!.id,
            path = input.pages?.single!!.path,
            title = input.pages?.single!!.title,
            description = input.pages?.single!!.description,
            content = removeEscapeSymbol(input.pages?.single?.content!!),
            render = renderToHtml(input.pages?.single?.render!!, true),
            createdAt = timeTransform(input.pages?.single!!.createdAt.toString()),
            updatedAt = timeTransform(input.pages?.single!!.updatedAt.toString()),
            contentType = input.pages?.single!!.contentType,
            authorID = input.pages?.single!!.authorId,
            authorEmail = input.pages?.single!!.authorEmail,
            authorName = input.pages?.single!!.authorName,
            creatorName = input.pages?.single!!.creatorName,
        ).apply {
            this.tags.addAll(tags)
            this.comments.addAll(comments)
            articleDao.insert(this)
        }
    }
}