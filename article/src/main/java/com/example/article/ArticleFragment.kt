package com.example.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.article.databinding.FragmentArticleBinding
import com.example.article.ui.ai.AiFragment
import com.example.article.ui.android.AndroidFragment
import com.example.article.ui.game.GameFragment
import com.example.article.ui.recommend.RecommendFragment
import com.example.article.ui.web.WebFragment
import com.google.android.material.tabs.TabLayoutMediator

class ArticleFragment : Fragment() {
    private lateinit var viewBinding: FragmentArticleBinding

    private val articleList = listOf("推荐","移动开发","Web开发","游戏开发","人工智能")
    private val fragmentList = listOf(
        RecommendFragment(),
        AndroidFragment(),
        WebFragment(),
        GameFragment(),
        AiFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentArticleBinding.inflate(inflater, container, false)
        viewBinding.articleViewpager.adapter = ArticleViewPagerAdapter(fragmentList,childFragmentManager,lifecycle)
        TabLayoutMediator(viewBinding.articleTableLayout,viewBinding.articleViewpager){tab, position ->
            tab.text = articleList[position]
        }.attach()
        return viewBinding.root
    }

}