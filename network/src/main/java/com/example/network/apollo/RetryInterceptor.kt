package com.example.network.apollo

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

class RetryInterceptor(
    private val maxRetry: Int//最大重试次数
) : Interceptor {
    private var retryNum = 0 //假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var response: Response = chain.proceed(request)
        Log.i("Retry", "num:$retryNum")
        while (!response.isSuccessful && retryNum < maxRetry) {
            retryNum++
            Log.i("Retry", "num:$retryNum")
            response = chain.proceed(request)
        }
        return response
    }
}