package com.example.csdapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.common.utils.hideSystemStatusBar
import com.example.csdapplication.databinding.ActivityMainBinding
import com.example.csdapplication.ui.TabLayoutAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val tabList = listOf("资料","部门","消息","更多")
    private val fragmentList = listOf(
        BlankFragment(),
        BlankFragment(),
        BlankFragment(),
        BlankFragment()
    )
    private val viewBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.hideSystemStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        bottomNavigationInit()
    }


    private fun bottomNavigationInit(){
        viewBinding.viewPager.adapter = TabLayoutAdapter(fragmentList,supportFragmentManager,lifecycle)
        TabLayoutMediator(viewBinding.tabLayout,viewBinding.viewPager){tab, position ->
            tab.text = tabList[position]
        }.attach()

        viewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                val animator1 = ObjectAnimator.ofFloat(tab.view, "scaleX", 1f, 1.2f,1f)
                val animator2 = ObjectAnimator.ofFloat(tab.view, "scaleY", 1f, 1.2f,1f)
                AnimatorSet().apply{
                    duration = 300
                    play(animator1).with(animator2)
                    start()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                Log.d("MainActivity","onTabUnselected")
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                Log.d("MainActivity","onTabReselected")
            }
        })
    }

}