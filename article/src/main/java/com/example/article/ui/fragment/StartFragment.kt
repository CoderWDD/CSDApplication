package com.example.article.ui.fragment

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.article.databinding.FragmentStartBinding
import com.example.article.ui.base.BaseFragment

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/09/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class StartFragment: BaseFragment() {

    private lateinit var  binding: FragmentStartBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentStartBinding.inflate(inflater, container, false)


        binding.animationView.speed = 1.5f
        binding.animationView.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                requireActivity().supportFragmentManager.popBackStack()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }
        })

        return  binding.root
    }




}