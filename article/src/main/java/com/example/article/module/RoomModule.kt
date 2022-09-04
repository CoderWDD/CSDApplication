package com.example.article.module

import android.app.Application
import androidx.room.Room
import com.example.article.data.local.ArticleDao
import com.example.article.data.local.ArticleDataBase
import com.example.article.data.local.BaseArticleDao
import com.example.article.data.local.FolderDao
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
object RoomModule {

    @Provides
    @Singleton
    fun provideAppDataBase(application: Application): ArticleDataBase {
        return Room
            .databaseBuilder(application, ArticleDataBase::class.java, "article_database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(articleDataBase: ArticleDataBase): ArticleDao{
        return articleDataBase.ArticleDao()
    }
    @Provides
    @Singleton
    fun provideBaseArticleDao(articleDataBase: ArticleDataBase): BaseArticleDao {
        return articleDataBase.BaseArticleDao()
    }

    @Provides
    @Singleton
    fun provideFolderNodeDao(articleDataBase: ArticleDataBase): FolderDao{
        return articleDataBase.FolderDao()
    }


}