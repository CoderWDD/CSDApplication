package com.example.article.data.remote

/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ErrorException(
    message: String,
    val code: Int,
    ): Exception(message) {



}