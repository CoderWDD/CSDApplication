package com.example.article.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.article.R
import com.example.article.ui.adapter.RecommendAdapter
import com.example.article.ui.base.BaseFragment
import com.example.article.ui.base.BaseRecyclerAdapter
import com.example.article.databinding.FragmentRecommendBinding
import com.example.article.data.remote.Response
import com.example.article.data.entity.Article
import com.example.article.data.entity.Tag
import com.example.article.data.remote.doError
import com.example.article.data.remote.doSuccess
import com.example.article.ext.getArticleDialog
import com.example.article.ui.adapter.FooterAdapter
import com.example.article.ui.adapter.HeaderAdapter
import com.example.article.ui.adapter.TagAdapter
import com.example.article.ui.inter.OnItemClickListener
import com.example.article.ui.inter.OnItemLongClickListener
import com.example.article.utils.ToastUtil
import com.example.article.ui.viewModel.ArticleViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@OptIn(FlowPreview::class)
class RecommendFragment : BaseFragment() {
    private lateinit var binding: FragmentRecommendBinding

    private val viewModel: ArticleViewModel by activityViewModels()

    private lateinit var recyclerAdapter: RecommendAdapter

    private lateinit var tagsAdapter: TagAdapter

    private lateinit var articleInfoDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendBinding.inflate(inflater, container, false)
        //init
        initRecyclerView()
        initArticleInfoDialog()

        return binding.root
    }

    /**
     * 初始化recyclerview
     * */
    private fun initRecyclerView() {
        recyclerAdapter = RecommendAdapter(requireContext())
        //获取数据源
        lifecycleScope.launch {
            viewModel.fetchRecommendBaseArticle().collect{
                recyclerAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            viewModel.recommendRefreshStateFlow.collectLatest {
                recyclerAdapter.refresh()
                viewModel.setRecommendRefresh(false)
            }
        }


        //点击进入文章
        recyclerAdapter.setOnItemClickListener(object : RecommendAdapter.OnItemClickListener{
            override fun onItemClick(holder: RecyclerView.ViewHolder, position: Int) {
                val data = recyclerAdapter.getBaseArticle(position)
                data?.let {
                    switchContentFragment(it.baseArticleID, it.path)
                }?: run {
                    ToastUtil.makeText(requireContext(), "加载失败~")
                }
            }
        })
        //长按显示文章基本内容
        recyclerAdapter.setOnItemLongClickListener(object : RecommendAdapter.OnItemLongClickListener{
            override fun onItemLongClick(holder: RecyclerView.ViewHolder, position: Int) {
                recyclerAdapter.getBaseArticle(position)?.let {
                    lifecycleScope.launch{
                        viewModel.fetchArticle(it.baseArticleID, it.path).collect{ response ->
                            response.doSuccess {
                                showArticleInfoDialog(it)
                            }
                            response.doError {
                                ToastUtil.makeText(requireContext(), "加载失败~")
                            }
                        }
                    }
                }?: run{
                    ToastUtil.makeText(requireContext(), "加载失败~")
                }
            }
        })
        //对加载数据的监听
        recyclerAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    binding.progressLoading.visibility = View.INVISIBLE
                    binding.recyclerViewRecommend.visibility = View.VISIBLE
                }
                is LoadState.Loading -> {
                    binding.progressLoading.visibility = View.VISIBLE
                    binding.recyclerViewRecommend.visibility = View.INVISIBLE
                }
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    binding.progressLoading.visibility = View.INVISIBLE
                    ToastUtil.makeText(requireContext(), "Load Error: ${state.error.message}" )
                }
            }
        }

        with(binding.recyclerViewRecommend){
            adapter = recyclerAdapter
                .withLoadStateFooter(FooterAdapter{recyclerAdapter.retry()})

            binding.recyclerViewRecommend.layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }
    }

    /**
     * 初始化文章信息的dialog
     * */
    private fun initArticleInfoDialog(){
        articleInfoDialog = AlertDialog.Builder(requireContext())
            .setTitle("文章信息")
            .setCancelable(true)
            .setPositiveButton("确认") { dialog, _ ->
                dialog.dismiss()
            }.create()

        tagsAdapter = TagAdapter(requireContext())

        tagsAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(holder: RecyclerView.ViewHolder, position: Int) {
                requireActivity().supportFragmentManager.beginTransaction().apply {

                    articleInfoDialog.dismiss()
                    val tag = tagsAdapter.getData(position)
                    add(R.id.container, TagRefFragment.newInstance(tag.tagID, tag.tag))
                    addToBackStack("tag_ref")
                    commit()
                }
            }
        })
    }

    /**
     * 在 [articleInfoDialog]上显示 [article] 信息
     * @param article 显示的文章
     * */
    private fun showArticleInfoDialog(article: Article){
        val dialogView = getArticleDialog(article, requireContext())
        dialogView.findViewById<RecyclerView>(R.id.recyclerView_dialog_tags).apply {
            tagsAdapter.updateData(article.tags)
            this.adapter = tagsAdapter
            this.layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }
        articleInfoDialog.setView(dialogView)
        articleInfoDialog.show()
    }

    /**切换至ContentFragment**/
    private fun switchContentFragment(articleID: Int, articlePath: String){
        requireActivity().supportFragmentManager.beginTransaction().apply {
            add(R.id.container, ContentFragment.newInstance(articleID, articlePath))
            addToBackStack("content")
            commit()
        }
    }
}