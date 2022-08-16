package com.example.article.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.article.adapter.PathAdapter
import com.example.article.databinding.FragmentPathBinding
import com.example.article.viewModel.ArticleViewModel

/**
 *     author : swk
 *     time   : 2022/08/13
 *     desc   : 文件路径Fragment
 *     version: 1.0
 */

private const val ARG_TAG = "tag"

class PathFragment : Fragment() {
    //fragment内容标志
    private var contentTag: String? = null

//    private val viewModel by activityViewModels<ArticleViewModel>()

    private lateinit var binding: FragmentPathBinding

    private lateinit var parent: ArticleFragment

    //是否打开一个contentFragment
    private var isOpen = false

    //文件绝对路径
    private val absolutePath = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contentTag = it.getString(ARG_TAG)
            if(absolutePath.isEmpty()){
                absolutePath.add(tag!!)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPathBinding.inflate(inflater)
        //父级fragment
        parent = parentFragment as ArticleFragment

        //init
        initPathDisplay()
        initPathRecyclerView()

        return binding.root
    }

    private fun initPathDisplay() {
        binding.tvPath.text = path()
    }

    private fun initPathRecyclerView() {
        val pathAdapter = PathAdapter(binding.recyclerViewPath).apply {
            setOnItemClickListener(object : PathAdapter.OnItemClickListener {
                override fun onItemClick(holder: PathAdapter.PathViewHolder, position: Int) {
                    //TODO: 更新文件路径
                    Log.d(contentTag, "onItemClick: xxx")

                    absolutePath.add(holder.tv_path.text.toString())
                    binding.tvPath.text = path()
                    change()
                }
            })
        }

        val linearLayoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recyclerViewPath.apply {
            adapter = pathAdapter
            layoutManager = linearLayoutManager
        }

        //处理返回事件
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner,
                object: OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if(absolutePath.size == 1){

                        }else {
                            absolutePath.removeAt(absolutePath.size - 1)
                            pathAdapter.preChange()
                            binding.tvPath.text = path()
                        }
                    }
                }
            )
    }

    /** 当前路径的显示 */
    private fun path(): String{
        val stringBuilder = StringBuilder()
        for (str in absolutePath){
            stringBuilder.append(str)
            stringBuilder.append(" / ")
        }
        return stringBuilder.toString()
    }


    /**
     * A simple [Fragment] subclass.
     * Use the [PathFragment.newInstance] factory method to
     * create an instance of this fragment.
     */
    companion object {
        @JvmStatic
        fun newInstance(TAG: String) =
            PathFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TAG, TAG)
                }
            }
    }

//    override fun onResume() {
//        super.onResume()
//        parent.setFABVisibility(
//            if(binding.nestedScrollView.scrollY != 0) View.VISIBLE
//            else View.GONE
//        )
//    }


    /** 初始化滑动窗口 监听滑动事件 */
//    private fun initNestedScrollView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            binding.nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//                when(scrollY){
//                    0 -> parent.setFABVisibility(View.GONE)
//                    else -> parent.setFABVisibility(View.VISIBLE)
//                }
//            }
//        }
//    }
    /** 设置NestedScrollView上划到最顶端 */
//    fun setNestedScrollViewTop(){
//        if(binding.nestedScrollView.scrollY != 0){
//            binding.nestedScrollView.fullScroll(ScrollView.FOCUS_UP)
//        }
//    }

}