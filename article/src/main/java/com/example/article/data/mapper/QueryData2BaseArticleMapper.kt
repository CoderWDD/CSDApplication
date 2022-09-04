package com.example.article.data.mapper

import com.example.article.ui.base.timeTransform
import com.example.article.data.entity.BaseArticle
import com.example.article.data.entity.relationship.article_tags.BaseArticleTagCrossRef
import com.example.article.utils.LocalStorageUtil
import com.example.model.BaseArticleQuery

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/27
 *     desc   : 实现将请求的数据转换为 展示在UI的实体类，并保存在数据库中
 *     version: 1.0
 * </pre>
 */

class QueryData2BaseArticleMapper: Mapper<BaseArticleQuery.Data, BaseArticle> {

    companion object{
        private val refDao = LocalStorageUtil.dataBase.ArticleTagCrossRefDao()
        private val baseArticleDao = LocalStorageUtil.dataBase.BaseArticleDao()
        private val tagDao = LocalStorageUtil.dataBase.TagDao()
        private val tagMapper = QueryData2TagMapper()
    }
    /**
     * 转换为BaseArticle
     * @param input 服务器返回的数据，注意不要为空
     * */
    override suspend fun map(input: BaseArticleQuery.Data): BaseArticle {
        return BaseArticle(
            baseArticleID = input.pages?.single?.id!!,
            updatedAt = timeTransform(input.pages?.single?.updatedAt!!.toString()),
            path = input.pages?.single?.path!!,
            title = input.pages?.single?.title!!,
        ).apply {
            input.pages?.single?.tags?.forEach {  tag->
                tag?.let {
                    refDao.insert(BaseArticleTagCrossRef(it.id, this.baseArticleID))
                    tagMapper.map(tag)
                }
            }
            baseArticleDao.insert(this)
        }
    }
    suspend fun map(input: BaseArticleQuery.Data, parentFolderID: Int): BaseArticle {
        return BaseArticle(
            baseArticleID = input.pages?.single?.id!!,
            updatedAt = timeTransform(input.pages?.single?.updatedAt!!.toString()),
            path = input.pages?.single?.path!!,
            title = input.pages?.single?.title!!,
            belongFolderID = parentFolderID
        ).apply {
            val refDao = LocalStorageUtil.dataBase.ArticleTagCrossRefDao()
            input.pages?.single?.tags?.forEach {  tag->
                tag?.let {
                    refDao.insert(BaseArticleTagCrossRef(it.id, this.baseArticleID))
                    tagMapper.map(tag)
                }
                baseArticleDao.insert(this)
            }
        }
    }
}