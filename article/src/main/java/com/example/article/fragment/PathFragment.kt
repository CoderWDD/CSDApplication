package com.example.article.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.article.R
import com.example.article.adapter.PathAdapter
import com.example.article.constants.Constants
import com.example.article.databinding.FragmentPathBinding
import com.example.article.viewModel.ArticleViewModel
import com.example.article.viewModel.PathViewModel

/**
 *     author : swk
 *     time   : 2022/08/13
 *     desc   : 文件路径Fragment
 *     version: 1.0
 */

private const val ARG_TAG = "TAG"
private const val ARG_SYMBOL = "SYMBOL"

class PathFragment : BaseFragment() {
    //fragment内容标志
    private var contentTag: Int? = null

    private val viewModel by lazy { ViewModelProvider(this)[PathViewModel::class.java] }

    private lateinit var binding: FragmentPathBinding

    private lateinit var pathAdapter: PathAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contentTag = it.getInt(ARG_TAG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPathBinding.inflate(inflater, container, false)

        //init
        initPathDisplay()
        initPathRecyclerView()

        return binding.root
    }




    //返回事件：返回上一级目录
    override fun onBackPressed(): Boolean {
        return if( viewModel.getPath(contentTag!!).size > 1){
            viewModel.removePath(contentTag!!)
            pathAdapter.preChange()
            binding.tvPath.text = pathToString()
            true
        }else super.onBackPressed()
    }


    /** 显示绝对路径 */
    private fun initPathDisplay() {
        binding.tvPath.text = pathToString()
    }

    private fun initPathRecyclerView() {
        pathAdapter = PathAdapter(binding.recyclerViewPath).apply {
            setOnItemClickListener(object : PathAdapter.OnItemClickListener {
                override fun onItemClick(holder: PathAdapter.PathViewHolder, position: Int) {
                    //TODO: 更新文件路
                    viewModel.addPath(contentTag!!, holder.tv_path.text.toString())
                    binding.tvPath.text = pathToString()
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

}

    /** 当前路径的显示 */
    private fun pathToString(): String{
        val stringBuilder = StringBuilder()
        for (str in viewModel.getPath(contentTag!!)){
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
        fun newInstance(TAG: Int) =
            PathFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TAG, TAG)
                }
            }
    }
}