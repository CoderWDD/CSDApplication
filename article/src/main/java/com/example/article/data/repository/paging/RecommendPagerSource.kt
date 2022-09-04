package com.example.article.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.article.data.entity.BaseArticle
import com.example.article.data.local.ArticleDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class RecommendPagerSource(
    private val database: ArticleDataBase,
    private val keywords: List<Int> = mutableListOf() //保持引用，方便修改数据源
): PagingSource<Int, BaseArticle>() {

    fun getKeywords() = keywords.toList()

    override fun getRefreshKey(state: PagingState<Int, BaseArticle>): Int {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BaseArticle> {
        return try {
            //获得当前页数
            val page = params.key ?: 1
            val tagDao = database.TagDao()

            val baseArticles = withContext(Dispatchers.IO){
                if(page <= keywords.size){
                    delay(1000)
                    mutableListOf<BaseArticle>().apply {
                        tagDao.getTagWithBaseArticles(keywords[page - 1])?.let {
                            this.addAll(it.baseArticles)
                        }
                    }
                }else emptyList()
            }

            val preKey = if(page > 1) page - 1 else null
            val nextKey = if(page <= keywords.size || baseArticles.isNotEmpty()) page + 1 else null
            LoadResult.Page(baseArticles, preKey, nextKey)
        } catch (e: Exception){
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}