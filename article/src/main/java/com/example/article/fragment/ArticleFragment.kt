package com.example.article.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.article.R
import com.example.article.databinding.FragmentInformationBinding
import com.example.article.adapter.ViewPagerAdapter
import com.example.article.viewModel.ArticleViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 *     author : swk
 *     time   : 2022/08/09
 *     desc   : ArticleFragment
 *     version: 1.0
 */

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ArticleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArticleFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

//    private val viewModel by  activityViewModels<ArticleViewModel>()

    private lateinit var binding: FragmentInformationBinding

    private lateinit var searchView: SearchView

    private var commentFragment: CommentFragment? = null

    private val tabList = listOf("主页", "移动开发", "WEB开发", "游戏开发", "人工智能")

    private val fragmentList = listOf(
        PathFragment.newInstance("home"),
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
//        Thread.sleep(1000)
        //viewBinding
        binding = FragmentInformationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
//        initBottomSheet()
//        initFabBack()
        initFabComment()

    }

    /** 对toolbar的searchView进行设置 */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar, menu)
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


    /** 上划按钮点击时整体回到最顶部  */
//    private fun initFabBack() {
//        binding.FABBackToTop.visibility = View.GONE
//        binding.FABBackToTop.setOnClickListener {
//            if(binding.FABBackToTop.visibility == View.VISIBLE){
//                //获得当前viewpager页面
//                val position = binding.viewPager.currentItem
//                fragmentList[position].setNestedScrollViewTop()
//                binding.layoutAppBar.setExpanded(true)
//            }
//        }
//    }





    /** 弹出CommentFragment */
    private fun initFabComment() {
        binding.FABComment.setOnClickListener {
            if(commentFragment != null){
                commentFragment?.show(childFragmentManager.beginTransaction(), commentFragment?.javaClass?.name)
            } else {
                commentFragment = CommentFragment()
                commentFragment?.show(childFragmentManager.beginTransaction(), commentFragment?.javaClass?.name)
            }
        }
    }

    /** 设置FAB_BackToTop 的可见度（pathFragment 使用） */
    fun setFABVisibility(visibility: Int){
        binding.FABBackToTop.visibility = visibility
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