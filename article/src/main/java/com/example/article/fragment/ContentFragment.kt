package com.example.article.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager.TAG
import com.example.article.databinding.FragmentContentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContentFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentContentBinding

    private var commentFragment: CommentFragment? = null

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
        binding = FragmentContentBinding.inflate(inflater, container, false)
        initFabComment()
        initFabBack()
        initScrollView()
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        binding.FABBackToTop.visibility =
            if(binding.nestedScrollView.scrollY != 0) View.VISIBLE
            else View.GONE
    }

    /**监听scrollView的滑动*/
    private fun initScrollView() {
        binding.nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if(scrollY != 0){
                binding.FABBackToTop.visibility = View.VISIBLE
            }else {
                binding.FABBackToTop.visibility = View.GONE
            }
        }
    }

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

    /** 上划按钮点击时整体回到最顶部  */
    private fun initFabBack() {
        binding.FABBackToTop.visibility = View.GONE
        binding.FABBackToTop.setOnClickListener {
            if(binding.FABBackToTop.visibility == View.VISIBLE){
                binding.nestedScrollView.fullScroll(View.FOCUS_UP)
                binding.layoutAppBar.setExpanded(true)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}