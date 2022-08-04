package com.example.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.constants.BuildConfig
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object Apollo {
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
            .serverUrl(BuildConfig.BASE_URL)
            .build()
    }
}