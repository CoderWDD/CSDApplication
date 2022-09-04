package com.example.article.utils


import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.NetworkCapabilities

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/26
 *     desc   : 判断网络是否可用
 *     version: 1.0
 * </pre>
 */
object NetWorkUtils {
    //判断网络状态，有网络返回true
    fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)

        return hasCapability(caps) || hasTransports(caps)
    }

    private fun hasCapability(caps: NetworkCapabilities?): Boolean{
        return caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)?: false
                && caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)?: false
    }

    private fun hasTransports(caps: NetworkCapabilities?): Boolean{
        return (caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)?: false
                || caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN)?:false)
                || caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)?: false
    }

}

