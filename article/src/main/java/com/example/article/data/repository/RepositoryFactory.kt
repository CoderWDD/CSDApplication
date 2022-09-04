package com.example.article.data.repository

import androidx.paging.PagingConfig
import com.apollographql.apollo3.ApolloClient
import com.example.article.data.local.ArticleDataBase
import com.example.article.data.mapper.QueryData2ArticleMapper
import com.example.article.data.mapper.QueryData2BaseArticleMapper
import com.example.article.data.mapper.QueryData2FolderMapper
import com.example.article.data.mapper.QueryData2TagMapper

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
object RepositoryFactory {

    fun makeArticleRepository(apolloClient: ApolloClient, dataBase: ArticleDataBase): ArticleRepository {
        return ArticleRepositoryImpl(
            apolloClient,
            dataBase,
            pagingConfig,
            QueryData2ArticleMapper (),
            QueryData2BaseArticleMapper(),
            QueryData2FolderMapper(),
            QueryData2TagMapper()
        )
    }
    private val pagingConfig = PagingConfig(
        // 每页显示的数据的大小
        pageSize = 5,

        // 开启占位符
        enablePlaceholders = true,

        // 预刷新的距离，距离最后一个 item 多远时加载数据
        // 默认为 pageSize
        prefetchDistance = 4,

        /**
         * 初始化加载数量，默认为 pageSize * 3
         *
         * internal const val DEFAULT_INITIAL_PAGE_MULTIPLIER = 3
         * val initialLoadSize: Int = pageSize * DEFAULT_INITIAL_PAGE_MULTIPLIER
         */
        initialLoadSize = 30

    )
}