package com.example.article.ext

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.example.article.R
import com.example.article.data.entity.Article
import com.example.article.data.remote.ErrorException
import com.example.article.data.remote.Response
import com.example.article.data.repository.ArticleRepository
import com.example.article.module.RepositoryModule
import com.example.article.ui.base.BaseFragment
import com.example.article.ui.viewModel.ArticleViewModel
import com.example.article.utils.LocalStorageUtil
import com.example.network.apollo.ApolloClient
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException
import kotlin.reflect.KClass

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
suspend fun <T: Operation.Data> execute(query: suspend ()-> ApolloResponse<T>): Flow<Response<T>> {
    return flow {
            val response = query()
            //处理返回结果
            with(response){
                if(hasErrors()){
                    errors?.let {
                        if(it.isNotEmpty()){
                            emit(Response.Error(
                                    ErrorException(it[0].message, it[0].extensions?.get("code") as Int)
                            ))
                        }
                    } ?: emit(Response.Error(ErrorException("未知错误！", -1)))
                }else{
                    emit(Response.Success(data!!))
                }
            }
    }.retryWhen{ cause, attempt ->  //重试
        //TODO: 对执行response的捕获错误处理
        if((cause is IOException || cause is Exception ) && attempt < 3){
            Log.d("Application", "execute: retry:>>>>>>>>>>>>>>>\n $attempt \t $cause ")
            true
        }else false
    }.catch { e->
        emit(Response.Error(e as Exception))
    }
}

/**
 * 带参的viewModel构造
 * */
@Suppress("UNCHECKED_CAST")
@OptIn(FlowPreview::class)
class ViewModelFactory constructor(
    private val application: Application,
    private val repository: ArticleRepository
): ViewModelProvider.AndroidViewModelFactory(application){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(ArticleViewModel::class.java)) ArticleViewModel(repository) as T
        else super.create(modelClass)
    }
}


fun getArticleDialog(article: Article, context: Context): View {
    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_article_info, null)
    dialogView.findViewById<TextView>(R.id.tv_dialog_article_title).text = handleSpannableText("标题: ", article.title)
    dialogView.findViewById<TextView>(R.id.tv_dialog_article_path).text =  handleSpannableText("路径: ", article.path)
    dialogView.findViewById<TextView>(R.id.tv_dialog_article_description).text = handleSpannableText("描述: ", article.description)
    dialogView.findViewById<TextView>(R.id.tv_dialog_article_create).text = handleSpannableText("上传时间: ", article.createdAt)
    dialogView.findViewById<TextView>(R.id.tv_dialog_article_update).text = handleSpannableText("最后编辑: ", article.updatedAt)
    dialogView.findViewById<TextView>(R.id.tv_dialog_article_author_name).text = handleSpannableText("作者名称: ",article.authorName)
    dialogView.findViewById<TextView>(R.id.tv_dialog_article_author_email).text = handleSpannableText("作者Email: ", article.authorEmail)
    dialogView.findViewById<TextView>(R.id.tv_dialog_article_comment_num).text = handleSpannableText("文章评论数: ", article.comments.size.toString())
    return dialogView
}


fun handleSpannableText(text: String, append: String): SpannableStringBuilder{
    return SpannableStringBuilder(text + append).apply {
        this.setSpan(ForegroundColorSpan(Color.parseColor("#0080ff")), 0, text.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        this.setSpan(ForegroundColorSpan(Color.parseColor("#000000")), text.length, text.length + append.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    }
}
