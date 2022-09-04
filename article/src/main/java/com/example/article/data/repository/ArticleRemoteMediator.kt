package com.example.article.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.apollographql.apollo3.ApolloClient
import com.example.article.AppHelper
import com.example.article.data.entity.Article
import com.example.article.data.local.ArticleDataBase
import com.example.article.utils.NetWorkUtils
import com.example.model.FolderTreeQuery
import retrofit2.HttpException
import java.io.IOException

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
//@OptIn(ExperimentalPagingApi::class)
//class ArticleRemoteMediator(
//    val apollo: ApolloClient,
//    val dataBase: ArticleDataBase
//) : RemoteMediator<Int, Article>(){
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, Article>
//    ): MediatorResult {
//
//        try {
//            val articleDao = dataBase.ArticleDao()
//            val baseArticleDao = dataBase.BaseArticleDao()
//
//            val pageKey = when(loadType){
//                //首次加载或者pageDataAdapter.refresh()刷新
//                LoadType.REFRESH -> null
//
//                LoadType.PREPEND -> {
//                    return MediatorResult.Success(endOfPaginationReached = true)
//                }
//                //上拉加载更多
//                LoadType.APPEND -> {
//                    //最后一条数据是下一页开始的标志
//                    //获取当前页面的最后一条数据
//                    val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
//                    lastItem.articleID
//                }
//            }
//            //没有网络加载本地数据
//            if(NetWorkUtils.isConnected(AppHelper.mContext)) {
//                return MediatorResult.Success(endOfPaginationReached = true)
//            }
//
//
//            //把网络加载的数据插入到数据库中
//            dataBase.withTransaction {
//
//            }
//
//
//        } catch (e: IOException){
//            return MediatorResult.Error(e)
//        } catch (e: HttpException){
//            return MediatorResult.Error(e)
//        }
//    }
//}