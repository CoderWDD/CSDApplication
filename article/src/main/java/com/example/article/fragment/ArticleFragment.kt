package com.example.article.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.article.R
import com.example.article.databinding.FragmentArticleBinding
import com.example.article.adapter.ViewPagerAdapter
import com.example.article.viewModel.ArticleViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.log

/**
 *     author : swk
 *     time   : 2022/08/09
 *     desc   : ArticleFragment
 *     version: 1.0
 */

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ArticleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArticleFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null

    private val viewModel by lazy { ViewModelProvider(this)[ArticleViewModel::class.java] }

    private lateinit var binding: FragmentArticleBinding

    private lateinit var searchView: SearchView


    private val tabList = listOf("推荐", "移动开发", "WEB开发", "游戏开发", "人工智能")

    private val fragmentList = listOf(
        RecommendFragment(),
        PathFragment.newInstance("移动开发"),
        PathFragment.newInstance("WEB开发"),
        PathFragment.newInstance("游戏开发"),
        PathFragment.newInstance("人工智能"),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //viewBinding
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        initViewPager()

        return binding.root
    }


    /** 对toolbar的searchView进行设置 */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_article_toolbar, menu)
        val searchItem = menu.getItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
    }

    /** 对menu的监听 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        return super.onOptionsItemSelected(item)
    }



    /** tabLayout绑定viewPager2 */
    private fun initViewPager(){
        //viewPager预加载
        binding.viewPager.offscreenPageLimit = fragmentList.size

        binding.viewPager.adapter = ViewPagerAdapter(fragmentList, childFragmentManager, requireActivity().lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position->
            tab.text = tabList[position]
        }.attach()

        //初始化时将当前tab进行缩放
        val animator1 = ObjectAnimator.ofFloat(binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)?.view, "scaleX", 1f, 1.3f)
        val animator2 = ObjectAnimator.ofFloat(binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)?.view, "scaleY", 1f, 1.3f)
        AnimatorSet().apply {
            duration = 300
            play(animator1).with(animator2)
            start()
        }

        //tab切换的动画
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                val animatorX = ObjectAnimator.ofFloat(tab.view, "scaleX", 1f, 1.3f)
                val animatorY = ObjectAnimator.ofFloat(tab.view, "scaleY", 1f, 1.3f)
                AnimatorSet().apply {
                    duration = 300
                    play(animatorX).with(animatorY)
                    start()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val animatorX = ObjectAnimator.ofFloat(tab.view, "scaleX", 1.3f, 1f)
                val animatorY = ObjectAnimator.ofFloat(tab.view, "scaleY", 1.3f, 1f)
                AnimatorSet().apply {
                    duration = 300
                    play(animatorX).with(animatorY)
                    start()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })



    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ArticleFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ArticleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}