package com.example.csdapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.example.article.ui.base.BaseActivity
import com.example.article.ui.fragment.ArticleFragment
import com.example.article.ui.fragment.StartFragment
import com.example.article.utils.BackHandlerHelper
import com.example.article.utils.ToastUtil
import com.example.common.utils.immersionStatusBar
import com.example.csdapplication.databinding.ActivityMainBinding
import com.example.csdapplication.ui.TabLayoutAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {
    //记录最后一次按下返回键的时间
    private var lastBackPress = 0L

    private val tabList = listOf("资料","部门","消息","更多")
    private val iconActiveList = listOf(
        R.drawable.article_active,
        R.drawable.department_active,
        R.drawable.message_active,
        R.drawable.more_active
    )
    private val iconInactiveList = listOf(
        R.drawable.article_inactive,
        R.drawable.department_inactive,
        R.drawable.message_inactive,
        R.drawable.more_inactive
    )
    private val fragmentList = listOf(
        ArticleFragment(),
        BlankFragment(),
        BlankFragment(),
        BlankFragment()
    )
    private val viewBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        this.immersionStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        lifecycleScope.launch(Dispatchers.IO){
            supportFragmentManager.beginTransaction().apply {
                add(R.id.container, StartFragment())
                addToBackStack("start")
                commit()
            }
        }


        bottomNavigationInit()
    }



    //点两次退出程序
    override fun onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)){
            if (System.currentTimeMillis() - lastBackPress < 1000) {
                super.onBackPressed()
            } else {
                lastBackPress = System.currentTimeMillis()
                ToastUtil.makeText(this, "再按一次退出")
            }
        }
    }

    private fun bottomNavigationInit(){
        viewBinding.viewPager.isUserInputEnabled = false
        viewBinding.viewPager.offscreenPageLimit = fragmentList.size
        viewBinding.viewPager.adapter = TabLayoutAdapter(fragmentList,supportFragmentManager,lifecycle)
        TabLayoutMediator(viewBinding.tabLayout,viewBinding.viewPager){tab, position ->
            tab.text = tabList[position]
            tab.icon = AppCompatResources.getDrawable(this,iconInactiveList[position])
        }.attach()
        viewBinding.tabLayout.getTabAt(0)!!.icon = AppCompatResources.getDrawable(this,iconActiveList[0])
        changeIconImgBottomMargin(viewBinding.tabLayout,0)

        viewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                val animator1 = ObjectAnimator.ofFloat(tab.view, "scaleX", 1f, 1.2f,1f)
                val animator2 = ObjectAnimator.ofFloat(tab.view, "scaleY", 1f, 1.2f,1f)
                AnimatorSet().apply{
                    duration = 300
                    play(animator1).with(animator2)
                    start()
                }
                tab.icon = AppCompatResources.getDrawable(this@MainActivity,iconActiveList[tab.position])
                changeIconImgBottomMargin(viewBinding.tabLayout,0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon = AppCompatResources.getDrawable(this@MainActivity,iconInactiveList[tab.position])
                changeIconImgBottomMargin(viewBinding.tabLayout,0)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                Log.d("MainActivity","onTabReselected")
            }
        })
    }

    private fun changeIconImgBottomMargin(parent: ViewGroup,px: Int){
        for (i in 0 until parent.childCount){
            val view = parent.getChildAt(i)
            if (view is ImageView){
                view.layoutParams.apply {
                    this as ViewGroup.MarginLayoutParams
                    this.bottomMargin = 0
                    this.topMargin = 0
                }
            }else if (view is ViewGroup){
                changeIconImgBottomMargin(view,px)
            }
        }
    }

}