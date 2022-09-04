package com.example.article.data.mapper

import com.example.article.data.entity.Tag
import com.example.article.data.local.TagDao
import com.example.article.utils.LocalStorageUtil
import com.example.model.ArticleQuery
import com.example.model.BaseArticleQuery
import com.example.model.TagsQuery


/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class QueryData2TagMapper: Mapper<ArticleQuery.Tag, Tag> {

    companion object{
        private val tagDao = LocalStorageUtil.dataBase.TagDao()
    }

    override suspend fun map(input: ArticleQuery.Tag): Tag {
        return Tag(
            tagID = input.id,
            tag = input.tag
        ).apply {
            tagDao.inset(this)
        }
    }
    suspend fun map(input: BaseArticleQuery.Tag): Tag {
        return Tag(
            tagID = input.id,
            tag = input.tag
        ).apply {
            tagDao.inset(this)
        }
    }
    suspend fun map(input: TagsQuery.Data): List<Tag> {
        return mutableListOf<Tag>().apply {
            input.pages?.tags?.forEach { tag ->
                tag?.let {
                    this.add(
                        Tag(
                            tagID = it.id,
                            tag = it.tag
                        ).apply {
                        }
                    )
                }
            }
            tagDao.inset(this)
        }
    }
}