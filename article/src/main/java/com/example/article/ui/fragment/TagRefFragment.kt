package com.example.article.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.LinearEasing
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.article.R
import com.example.article.data.remote.doEmpty
import com.example.article.data.remote.doError
import com.example.article.data.remote.doSuccess
import com.example.article.databinding.FragmentContentBinding
import com.example.article.databinding.FragmentTagRefBinding
import com.example.article.ext.ViewModelFactory
import com.example.article.module.RepositoryModule
import com.example.article.ui.adapter.TagRefAdapter
import com.example.article.ui.base.BaseFragment
import com.example.article.ui.base.BaseRecyclerAdapter
import com.example.article.ui.viewModel.ArticleViewModel
import com.example.article.utils.LocalStorageUtil
import com.example.network.apollo.ApolloClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(FlowPreview::class)
class TagRefFragment : BaseFragment() {

    private lateinit var binding: FragmentTagRefBinding

    private lateinit var adapter: TagRefAdapter


    private val viewModel: ArticleViewModel by activityViewModels{
        ViewModelFactory(requireActivity().application, RepositoryModule.provideArticleRepository(
            ApolloClient.apollo, LocalStorageUtil.dataBase))
    }

    private var tagID: Int? = null
    private var tagContent: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tagID = it.getInt(ARG_TAG_ID)
            tagContent = it.getString(ARG_TAG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTagRefBinding.inflate(inflater, container, false)

        initToolBar()
        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(tagID != null){
            lifecycleScope.launch(Dispatchers.IO){
                viewModel.fetchTagWithBaseArticle(tagID!!)
                    .onStart {
                        binding.progressbarLoading.visibility = View.VISIBLE
                        binding.recyclerViewTagRef.visibility = View.INVISIBLE
                    }
                    .onCompletion {
                        binding.progressbarLoading.visibility = View.INVISIBLE
                    }
                    .collect{response->
                        withContext(Dispatchers.Main) {
                            response.doSuccess {
                                adapter.updateData(it)
                                binding.recyclerViewTagRef.visibility = View.VISIBLE
                            }
                            response.doError {
                                Snackbar.make(binding.root, "加载失败", Snackbar.LENGTH_INDEFINITE).show()
                            }
                            response.doEmpty {
                                Snackbar.make(binding.root, "暂无此内容", Snackbar.LENGTH_INDEFINITE).show()
                            }
                        }
                }
            }
        }


    }

    override fun onBackPressed(): Boolean {
        requireActivity().supportFragmentManager.popBackStack()
        return true
    }



    private fun initToolBar(){
        binding.toolbarTagRefContent.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.toolbarTagRefContent.title = "与 $tagContent 相关的文章"
    }


    private fun initRecyclerView(){
        adapter = TagRefAdapter(requireContext())

        adapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<TagRefAdapter.TagRefViewHolder>{
            override fun onItemClick(holder: TagRefAdapter.TagRefViewHolder, position: Int) {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    val click = adapter.getData(position)
                    add(R.id.container, ContentFragment.newInstance(click.baseArticleID, click.path))
                    addToBackStack("content")
                    commit()
                }
            }
        })

        binding.recyclerViewTagRef.adapter = adapter
        binding.recyclerViewTagRef.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
    }










    companion object {
        private const val ARG_TAG = "tag"
        private const val ARG_TAG_ID = "tag_id"
        @JvmStatic
        fun newInstance(tagID: Int, tag: String) =
            TagRefFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TAG_ID, tagID)
                    putString(ARG_TAG, tag)
                }
            }
    }
}