package com.example.network.apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.apollographql.apollo3.network.okHttpClient
import com.example.constants.BuildConfig
import dagger.hilt.InstallIn
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


object ApolloClient{

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(ApolloAuthorizationInterceptor())
            .addInterceptor(RetryInterceptor(3))
            .connectTimeout(5000L,TimeUnit.MILLISECONDS)
            .readTimeout(5000L,TimeUnit.MILLISECONDS)
            .writeTimeout(5000L,TimeUnit.MILLISECONDS)
            .build()
    }


    val apollo : ApolloClient by lazy {
        ApolloClient.Builder()
            .okHttpClient(okHttpClient = okHttpClient)
            .addHttpInterceptor(LoggingInterceptor(level = LoggingInterceptor.Level.BODY))
            .serverUrl(BuildConfig.WIKI_URL)
            .build()
    }
}