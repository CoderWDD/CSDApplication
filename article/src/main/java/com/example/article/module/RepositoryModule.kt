package com.example.article.module

import com.apollographql.apollo3.ApolloClient
import com.example.article.data.repository.RepositoryFactory
import com.example.article.data.local.ArticleDataBase
import com.example.article.data.repository.ArticleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideArticleRepository(apolloClient: ApolloClient, database: ArticleDataBase): ArticleRepository{
        return RepositoryFactory.makeArticleRepository(apolloClient, database)
    }
}