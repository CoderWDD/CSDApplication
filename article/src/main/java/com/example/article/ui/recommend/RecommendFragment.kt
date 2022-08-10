package com.example.article.ui.recommend

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.article.databinding.FragmentRecommendBinding
import com.example.model.TestQuery
import com.example.network.apollo.ApolloClient
import kotlinx.coroutines.launch

class RecommendFragment : Fragment() {
    private lateinit var viewBinding: FragmentRecommendBinding
    private lateinit var viewModel: RecommendViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentRecommendBinding.inflate(inflater, container, false)
        viewBinding.buttonTest.setOnClickListener {
            lifecycleScope.launch {
                val response = ApolloClient.apollo.query(TestQuery()).execute()
                Log.e("wgw", "onActivityCreated: ${response.data}", )
            }
        }
        return viewBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RecommendViewModel::class.java]
    }


}