package com.example.network.apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.constants.BuildConfig
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ApolloClient {
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(ApolloAuthorizationInterceptor())
            .connectTimeout(5000L,TimeUnit.MILLISECONDS)
            .readTimeout(5000L,TimeUnit.MILLISECONDS)
            .writeTimeout(5000L,TimeUnit.MILLISECONDS)
            .build()
    }

    val apollo : ApolloClient by lazy {
        ApolloClient.Builder()
            .okHttpClient(okHttpClient = okHttpClient)
            .serverUrl(BuildConfig.WIKI_BASE_URL)
            .build()
    }
}