package com.example.article.data.remote

/**
 *     author : 叶梦璃愁
 *     time   : 2022/08/19
 *     desc   : response
 *     version: 1.0
 */
sealed class Response<out T>(val status: ResponseStatus) {
    /**成功响应*/
    data class Success<out R>(val value: R): Response<R>(status = ResponseStatus.Success)
    /**出错*/
    data class Error(val exception: Exception): Response<Nothing>(status = ResponseStatus.Error)
    /**返回数据为空*/
    object Empty: Response<Nothing>(status = ResponseStatus.Empty)
    /**加载中*/
    object Loading: Response<Nothing>(status = ResponseStatus.Loading)
}

inline fun <reified T> Response<T>.doSuccess(success: (T) -> Unit) {
    if (this is Response.Success) {
        success(value)
    }
}

inline fun <reified T> Response<T>.doError(error: (Throwable?) -> Unit) {
    if (this is Response.Error) {
        error(exception)
    }
}

inline fun <reified T>Response<T>.doEmpty(empty: () -> Unit){
    if(this is Response.Empty){
        empty()
    }
}

inline fun <reified T>Response<T>.doLoading(loading: () -> Unit){
    if(this is Response.Loading){
        loading()
    }
}

enum class ResponseStatus{
    Success, Empty, Error, Loading
}
