package com.example.article.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.example.article.AppHelper
import com.example.article.data.entity.Article
import com.example.article.data.entity.BaseArticle
import com.example.article.data.entity.Folder
import com.example.article.data.entity.Tag
import com.example.article.data.entity.relationship.article_tags.Tag2BaseArticles
import com.example.article.data.local.ArticleDataBase
import com.example.article.data.local.TagDao
import com.example.article.data.mapper.QueryData2ArticleMapper
import com.example.article.data.mapper.QueryData2BaseArticleMapper
import com.example.article.data.mapper.QueryData2FolderMapper
import com.example.article.data.mapper.QueryData2TagMapper
import com.example.article.data.remote.ErrorException
import com.example.article.data.remote.Response
import com.example.article.data.remote.doError
import com.example.article.data.remote.doSuccess
import com.example.article.data.repository.paging.RecommendPagerSource
import com.example.article.ext.execute
import com.example.article.utils.NetWorkUtils
import com.example.model.ArticleQuery
import com.example.model.BaseArticleQuery
import com.example.model.FolderTreeQuery
import com.example.model.TagsQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ArticleRepositoryImpl(
    private val apollo: ApolloClient,
    private val dataBase: ArticleDataBase,
    private val pagingConfig: PagingConfig,
    private val articleMapper: QueryData2ArticleMapper,
    private val baseArticleMapper: QueryData2BaseArticleMapper,
    private val folderMapper: QueryData2FolderMapper,
    private val tagMapper: QueryData2TagMapper
): ArticleRepository {

    private val articleDao = dataBase.ArticleDao()
    private val baseArticleDao = dataBase.BaseArticleDao()
    private val folderDao = dataBase.FolderDao()
    private val tagDao = dataBase.TagDao()
    private val articleTagCrossRefDao = dataBase.ArticleTagCrossRefDao()

    private val TAG = "ArticleRepository"
    /**
     * 获取文件路径结构
     * @param folderID 当前文件夹的ID
     * @param title 当前文件夹的名字
     * @param path 当前文件夹的路径
     * @param parentFolderID 当前文件夹的上一级文件夹
     * @param curFolder 待处理的文件夹（需要处理的是list）
     * */
    override suspend fun fetchFolder(folderID: Int, title: String, path: String, parentFolderID: Int, curFolder: Folder?): Flow<Response<Folder>> {
        return flow {
            //有网络则加载网络数据，否则加载本地数据，二者选其一不能混合
            if(NetWorkUtils.isConnected(AppHelper.mContext)){
                execute {
                    apollo.query(FolderTreeQuery(parentId = folderID)).execute()
                }.collect{ response->
                    response.doSuccess { data ->
                        val folder = curFolder ?: Folder(folderID, title, path, parentFolderID)
                        //处理数据
                        folderMapper.map(data, folder)
                        folder.childFolders.folders.forEach {
                            it.parent = folder
                        }
                        //处理baseArticles
                        folder.baseArticleIDs.forEach { baseArticleID->
                            fetchBaseArticle(baseArticleID, folderID).collect{ baseArticle->
                                baseArticle.doSuccess {
                                    folder.baseArticles.baseArticles.add(
                                        it.apply {
                                            belongFolderID = folderID
                                        }
                                    )
                                }
                            }
                        }
                        folderDao.insert(folder)
                        emit(Response.Success(folder))
                    }
                    response.doError {
                        it?.let { throw it }
                    }
                }
            }else { //无网络时加载本地数据
                Log.d(TAG, "fetchFolder: >>>>>>>>>>>>>>no network!")
                val folder = folderDao.getFolder(folderID)
                if(folder != null){
                    folder.childFolders = folderDao.getChildFolder(folderID)
                    folder.baseArticles = folderDao.getChildBaseArticle(folderID)
                    emit(Response.Success(folder))
                }
            }
        }
            .retryWhen{_, attempt ->
                attempt < 3
            }
            .flowOn(Dispatchers.IO)
            .catch {e->
                e.printStackTrace()
            }
    }

    /**
     * 从服务器中获取baseArticle
     * @param baseArticleID 文章ID
     * @param parentFolderID 所属文件夹的ID （默认值为-1）
     * */
    override suspend fun fetchBaseArticle(baseArticleID: Int, parentFolderID: Int): Flow<Response<BaseArticle>> {
        return flow{
            try{
                execute {
                    apollo.query(BaseArticleQuery(baseArticleID)).execute()
                }.collect{ it ->
                    it.doSuccess {
                        val baseArticle = if(parentFolderID == -1) baseArticleMapper.map(it) else baseArticleMapper.map(it, parentFolderID)
                        emit(Response.Success(baseArticle))
                    }
                    it.doError {
                        it?.let { throw it }
                    }
                }
            }catch (e: Exception){
                emit(Response.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取文章
     * @param articleID 文章的id
     * @param path 文章的路径
     * @param parentFolderID 文章的所属文件夹的id， 默认为0
     * */
    override suspend fun fetchArticle(articleID: Int, path: String, parentFolderID: Int): Flow<Response<Article>> {
        return flow {

                //本地数据库查询
                val localArticle = articleDao.getArticle(articleID)
                //网络数据申请baseArticle, 获取文章的数据
                var baseArticle: BaseArticle? = null
                if(NetWorkUtils.isConnected(AppHelper.mContext)) {
                    fetchBaseArticle(articleID).collect{ response->
                        response.doSuccess {
                            baseArticle = it
                            baseArticleDao.insert(it)
                        }
                    }
                }else {//没有网络从本地加载
                    baseArticle = baseArticleDao.getBaseArticle(articleID)
                }

                //本地和网络都没有加载出来
                if(baseArticle == null) {
                    emit(Response.Empty)
                    return@flow
                }

                if(localArticle != null) {
                    if (baseArticle!!.baseArticleID == localArticle.articleID
                        && baseArticle!!.updatedAt == localArticle.updatedAt
                    ) { //文章没有更新，则使用本地数据
                        emit(Response.Success(localArticle))
                        return@flow
                    }
                }else{
                    //本地没有数据以及本地数据过时
                    execute {
                        apollo.query(ArticleQuery(articleID, path)).execute()
                    }.collect{ response->
                        //网络请求成功
                        response.doSuccess {
                            val article = articleMapper.map(it)
                            emit(Response.Success(article))
                        }
                        response.doError {
                            emit(Response.Error(it as Exception))
                        }
                    }
                }
            }
            .flowOn(Dispatchers.IO)
            .catch {e->
                emit(Response.Error(e as Exception))
            }
    }

    /**
     * 通过[query]来查询文章
     * @param query 查询字符串
     * @return 返回本地数据库匹配项
     * */
    override suspend fun fetchBaseArticleByQuery(query: String): Flow<PagingData<BaseArticle>> {
        return Pager(pagingConfig) {
            baseArticleDao.getBaseArticleByQuery(query)
        }.flow.flowOn(Dispatchers.IO)
    }

    /**返回推荐文章设置的数据*/
    override fun fetchRecommendBaseArticle(keywords: List<Int>): Flow<PagingData<BaseArticle>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                RecommendPagerSource(dataBase, keywords)
            }
        ).flow.flowOn(Dispatchers.IO)
    }

    /**
     * 获取全部tag
     * */
    override suspend fun fetchTags(): Flow<Response<List<Tag>>> {
        return flow{
            //网络获取
            if(NetWorkUtils.isConnected(AppHelper.mContext)){
                execute {
                    apollo.query(TagsQuery()).execute()
                }.collect{ response->
                    response.doSuccess {
                        tagDao.deleteAll()
                        tagMapper.map(it).apply {
                            emit(Response.Success(this))
                        }
                    }
                    response.doError {
                        it?.let { throw it }
                    }
                }
            }else{
                tagDao.getAll().apply {
                    if(this.isNotEmpty()) emit(Response.Success(this))
                    else emit(Response.Empty)
                }
            }
        }
            .catch { cause ->
                cause.printStackTrace()
                emit(Response.Error(cause as Exception))
            }
            .flowOn(Dispatchers.IO)
            .retryWhen{ _, attempt ->
                attempt < 3
            }
    }

    override suspend fun fetchTagWithBaseArticles(tagID: Int): Flow<Response<List<BaseArticle>>> {
        return flow {
            val data = tagDao.getTagWithBaseArticles(tagID)
            if(data != null){
                if(data.baseArticles.isEmpty()){
                    emit(Response.Empty)
                }else emit(Response.Success(data.baseArticles))
            } else {
                emit(Response.Error(ErrorException("no such data in database!", -100)))
            }
        }.flowOn(Dispatchers.IO)
    }
}