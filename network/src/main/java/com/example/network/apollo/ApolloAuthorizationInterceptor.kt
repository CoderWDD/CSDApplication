package com.example.network.apollo

import com.example.constants.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApolloAuthorizationInterceptor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.WIKI_TOKEN}")
            .build()
        return chain.proceed(newRequest)

    }
}