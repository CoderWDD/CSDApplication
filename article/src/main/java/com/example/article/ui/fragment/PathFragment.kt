package com.example.article.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.article.R
import com.example.article.constants.Constants
import com.example.article.data.entity.BaseArticle
import com.example.article.data.entity.Folder
import com.example.article.ui.adapter.PathAdapter
import com.example.article.ui.base.BaseFragment
import com.example.article.ui.base.BaseRecyclerAdapter
import com.example.article.databinding.FragmentPathBinding
import com.example.article.utils.ToastUtil
import com.example.article.ui.viewModel.ArticleViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

/**
 *     author : 叶梦璃愁
 *     time   : 2022/08/13
 *     desc   : 文件路径Fragment
 *     version: 1.0
 */
@OptIn(FlowPreview::class)
class PathFragment : BaseFragment() {
    //fragment内容标志
    private var tag: Int? = null

    private var TAG = ""

    private val viewModel: ArticleViewModel by activityViewModels ()

    private lateinit var binding: FragmentPathBinding

    private lateinit var pathAdapter: PathAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tag = it.getInt(ARG_TAG)

            TAG = when(tag){
                Constants.Android-> "安卓开发PathFragment"
                Constants.AI-> "人工智能PathFragment"
                Constants.Web-> "Web开发PathFragment"
                Constants.Game -> "游戏开发PathFragment"
                else -> "出错PathFragment"
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPathBinding.inflate(inflater, container, false)


        initPathRecyclerView()
        //init
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.getFolderStateFlow(tag!!)
                    ?.onCompletion {
                        //TODO: 文件夹更新过后的操作
                    }
                    ?.collect {
                    Log.d(TAG, "onCreateView: >>>>>>>>>>>>> folder:${it?.path ?: "null"}")
                        pathAdapter.updateFilePath(it)
                        binding.tvPath.text = it?.path ?: "null"
                    }
            }
        }
        return binding.root
    }

    //返回事件：返回上一级目录
    override fun onBackPressed(): Boolean {
        /**
         * 返回上一级的条件:
         * - viewpager当前fragment为该fragment
         * - 当前folder的父文件夹ID不为0（0为根目录的ID）
         */
        val parentFolder =  viewModel.getFolder(tag)?.parent
        return if(viewModel.curItem == tag && parentFolder != null && parentFolder.folderID != 0){
            ToastUtil.makeText(requireContext(), "$TAG  返回上一级")
            viewModel.setPreFolder(tag)
            true
        }else super.onBackPressed()
    }


    private fun initPathRecyclerView() {
        pathAdapter = PathAdapter(requireContext()).apply {
            setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<PathAdapter.PathViewHolder>{
                override fun onItemClick(holder: PathAdapter.PathViewHolder, position: Int) {
                    val click = pathAdapter.getFile(position)
                    ToastUtil.makeText(requireContext(), "点击了 ${click.title}")
                    //文件夹进入子文件夹
                    if(click is Folder){
                        viewModel.setNextFolder(tag, pathAdapter.folderFromFiles(position))
                    }else if(click is BaseArticle){
                        //打开文章
                        requireActivity().supportFragmentManager.beginTransaction().apply {
                            add( R.id.container, ContentFragment.newInstance(click.baseArticleID, click.path))
                            addToBackStack("content")
                            setReorderingAllowed(true)
                            commit()
                        }
                    }
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

    companion object {
        private const val ARG_TAG = "TAG"
        private const val ARG_SYMBOL = "SYMBOL"
        @JvmStatic
        fun newInstance(TAG: Int) =
            PathFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TAG, TAG)
                }
            }
    }
}