package com.example.article.data.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.article.data.entity.Article
import com.example.article.data.entity.BaseArticle
import com.example.article.data.entity.Folder
import com.example.article.data.entity.IFile
import com.example.article.data.entity.Tag
import com.example.article.data.entity.relationship.article_tags.Tag2BaseArticles
import com.example.article.data.remote.Response
import com.example.model.BaseArticleQuery
import kotlinx.coroutines.flow.Flow
import java.io.File

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface ArticleRepository {

    suspend fun fetchFolder(folderID: Int, title: String, path: String, parentFolderID: Int, curFolder: Folder? = null): Flow<Response<Folder>>

    suspend fun fetchBaseArticle(baseArticleID: Int, parentFolderID: Int = -1): Flow<Response<BaseArticle>>

    suspend fun fetchArticle(articleID: Int, path: String,  parentFolderID: Int = -1): Flow<Response<Article>>

    suspend fun fetchBaseArticleByQuery(query: String): Flow<PagingData<BaseArticle>>

    fun fetchRecommendBaseArticle(keywords: List<Int>): Flow<PagingData<BaseArticle>>

    suspend fun fetchTags(): Flow<Response<List<Tag>>>

    suspend fun fetchTagWithBaseArticles(tagID: Int): Flow<Response<List<BaseArticle>>>
}
