package com.example.article.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager.TAG
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.article.R
import com.example.article.databinding.FragmentRecommendBinding
import com.example.article.viewModel.RecommendViewModel
import com.example.model.TestQuery
import com.example.network.apollo.ApolloClient
import kotlinx.coroutines.launch

class RecommendFragment : BaseFragment() {
    private lateinit var binding: FragmentRecommendBinding
    private lateinit var viewModel: RecommendViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendBinding.inflate(inflater, container, false)

        binding.buttonTest.setOnClickListener{

            requireActivity().supportFragmentManager.beginTransaction().apply {
                add(R.id.container, ContentFragment())
                addToBackStack(null)
                commit()
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RecommendViewModel::class.java]
    }


}