package com.example.article.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.article.R

import retrofit2.http.Header

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/09/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class HeaderAdapter(private val refresh:()->Unit): LoadStateAdapter<HeaderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.header_refresh, parent, false))
    }

}