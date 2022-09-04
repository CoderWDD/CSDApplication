package com.example.article.ui.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.article.R
import com.example.article.constants.Constants
import com.example.article.data.entity.Article
import com.example.article.data.remote.doError
import com.example.article.data.remote.doSuccess
import com.example.article.databinding.FragmentContentBinding
import com.example.article.ext.ViewModelFactory
import com.example.article.ext.handleSpannableText
import com.example.article.module.RepositoryModule
import com.example.article.ui.adapter.TagAdapter
import com.example.article.ui.base.BaseActivity
import com.example.article.ui.base.BaseFragment
import com.example.article.ui.inter.FragmentOnTouchListener
import com.example.article.ui.inter.GestureListener
import com.example.article.ui.inter.OnItemClickListener
import com.example.article.ui.viewModel.ArticleViewModel
import com.example.article.utils.LocalStorageUtil
import com.example.article.utils.WebViewUtil
import com.example.network.apollo.ApolloClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/09
 *     desc   : ContentFragment 显示文章详细内容
 *     version: 1.0
 * </pre>
 */
@OptIn(FlowPreview::class)
class ContentFragment : BaseFragment() {

    companion object {
        private const val ARG_ARTICLE_ID = "articleID"
        private const val ARG_ARTICLE_PATH = "articlePath"

        @JvmStatic
        fun newInstance(articleID: Int, articlePath: String) =
            ContentFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ARTICLE_ID, articleID)
                    putString(ARG_ARTICLE_PATH, articlePath)
                }
            }
    }
    //当前文章ID
    private var articleID by Delegates.notNull<Int>()
    private lateinit var articlePath: String

    //当前Article
    private var article: Article? = null

    private lateinit var binding: FragmentContentBinding

    private var commentFragment: CommentFragment? = null

    private val viewModel: ArticleViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, RepositoryModule.provideArticleRepository(
            ApolloClient.apollo, LocalStorageUtil.dataBase))
    }
    //动画
    private val fabUpCollapseAnimatorSet = AnimatorSet()
    private val fabUpExpandAnimatorSet =  AnimatorSet()
    private val fabComCollapseAnimatorSet = AnimatorSet()
    private val fabComExpandAnimatorSet =  AnimatorSet()

    //监听触摸事件
    private var fragmentOnTouchListener: FragmentOnTouchListener? = null

    //显示标签的adapter
    private lateinit var tagAdapter: TagAdapter

    private var isFullScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            articleID = it.getInt(ARG_ARTICLE_ID)
            articlePath = it.getString(ARG_ARTICLE_PATH).toString()
        }?: run{
            articleID = Constants.SOMETHING_WRONG
            articlePath = ""
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentBinding.inflate(inflater, container, false)

        initAnimation()
        initSwipeRefreshLayout()
        initFabComment()
        initFabBack()
//        initToolbar()
        initScrollView()
        initArticle()
        initFullScreen()

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        binding.fabBackToTop.visibility =
            if(binding.nestedScrollView.scrollY != 0) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
    }

    //处理返回事件，为退出当前Fragment
    override fun onBackPressed(): Boolean {
        requireActivity().supportFragmentManager.popBackStack()
        return true
    }

    override fun onDestroy() {
        //注销在Activity的注册
        if(fragmentOnTouchListener != null){
            (requireActivity() as BaseActivity).unregisterMyOnTouchListener(listener = fragmentOnTouchListener!!)
        }
        super.onDestroy()
    }

    /**配置全屏*/
    private fun initFullScreen() {

        val mGestureListener = GestureListener()

        val gestureDetector = GestureDetectorCompat(requireContext(), mGestureListener).apply {
            setIsLongpressEnabled(true)
            setOnDoubleTapListener(mGestureListener)
        }
        fragmentOnTouchListener = object :FragmentOnTouchListener{
            override fun onTouch(event: MotionEvent?) {
                if (event != null) {
                    gestureDetector.onTouchEvent(event)
                }
            }
        }
        (requireActivity() as BaseActivity).registerFragmentListener(fragmentOnTouchListener!!)
    }

    //TODO: 把数据转为viewModel里面
    /** 显示文章信息 */
    private fun initArticle() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.fetchArticle(articleID, articlePath)
                    .onStart {
                        withContext(Dispatchers.Main) {
                            binding.swipeRefreshLayoutContent.isRefreshing = true
                        }
                    }
                    .onCompletion {
                        if (article != null) {
                            //UI更新操作需要在主线程执行
                            withContext(Dispatchers.Main) {
                                //更新完以后把数据显示在UI
                                binding.tvArticleUpdateTime.text = handleSpannableText("编辑时间: ", article?.updatedAt!!)
                                binding.tvArticleAuthor.text = handleSpannableText("作者: ", article?.authorName!!)
                                binding.tvArticleCreatedTime.text = handleSpannableText("上传时间: ", article?.createdAt!!)
                                binding.tvArticleUpdater.text = handleSpannableText("最后编辑: ", "暂无设置")
                                initWebView()
                                initTagRecyclerView()
                                binding.swipeRefreshLayoutContent.isRefreshing = false
                            }
                        }else {


                        }
                    }
                    .collect { response->
                        response.doSuccess {
                            article = it
                        }
                        response.doError {
                            Snackbar.make(binding.root, "加载出错辽~", Snackbar.LENGTH_INDEFINITE).show()
                        }
                    }
            }
        }
    }

    /**动画*/
    private fun initAnimation() {
        val fabCollapseAnimationX = ObjectAnimator.ofFloat(binding.fabBackToTop, "scaleX", 1f, 0f)
        val fabCollapseAnimationY = ObjectAnimator.ofFloat(binding.fabBackToTop, "scaleY", 1f, 0f)
        val fabExpandAnimationX = ObjectAnimator.ofFloat(binding.fabBackToTop, "scaleX", 0f, 1f)
        val fabExpandAnimationY = ObjectAnimator.ofFloat(binding.fabBackToTop, "scaleY", 0f, 1f)

        val fabCollapseAnimationX1 = ObjectAnimator.ofFloat(binding.fabComment, "scaleX", 1f, 0f)
        val fabCollapseAnimationY1 = ObjectAnimator.ofFloat(binding.fabComment, "scaleY", 1f, 0f)
        val fabExpandAnimationX1 = ObjectAnimator.ofFloat(binding.fabComment, "scaleX", 0f, 1f)
        val fabExpandAnimationY1 = ObjectAnimator.ofFloat(binding.fabComment, "scaleY", 0f, 1f)

        fabUpCollapseAnimatorSet.apply {
            duration = 300
            play(fabCollapseAnimationX).with(fabCollapseAnimationY)
            addListener(object :Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {
                    binding.fabBackToTop.visibility = View.VISIBLE
                }
                override fun onAnimationEnd(animation: Animator?) {
                    binding.fabBackToTop.visibility = View.INVISIBLE
                }
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })
        }
        fabUpExpandAnimatorSet.apply {
            duration = 300
            play(fabExpandAnimationX).with(fabExpandAnimationY)
            addListener(object :Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {
                    binding.fabBackToTop.visibility = View.VISIBLE
                }
                override fun onAnimationEnd(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })
        }

        fabComCollapseAnimatorSet.apply {
            duration = 300
            play(fabCollapseAnimationX1).with(fabCollapseAnimationY1)
            addListener(object :Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {
                    binding.fabComment.visibility = View.VISIBLE
                }
                override fun onAnimationEnd(animation: Animator?) {
                    binding.fabComment.visibility = View.INVISIBLE
                }
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })
        }
        fabComExpandAnimatorSet.apply {
            duration = 300
            play(fabExpandAnimationX1).with(fabExpandAnimationY1)
            addListener(object :Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {
                    binding.fabComment.visibility = View.VISIBLE
                }
                override fun onAnimationEnd(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })
        }
    }


    /** 监听刷新状态
     */
    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayoutContent.isEnabled = false
        //TODO: 刷新文章内容
    }

    /**加载文章数据*/
    private fun initWebView() {
        WebViewUtil.setWebView(webView = binding.webViewContent)

        binding.webViewContent.loadDataWithBaseURL("https://wiki.dsstudio.tech", article?.render!!, "text/html", "UTF-8", "")

        binding.toolbar.title = article?.title
    }

    /**监听scrollView的滑动*/
    private fun initScrollView() {
        binding.nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            //从顶部下滑button展开
            if(scrollY != 0 && binding.fabBackToTop.visibility == View.INVISIBLE){
                fabUpExpandAnimatorSet.start()

            //回到顶部
            }else if(scrollY == 0 && binding.fabBackToTop.visibility == View.VISIBLE){
                fabUpCollapseAnimatorSet.start()
            }
        }
    }

    /** 弹出CommentFragment */
    private fun initFabComment() {
        binding.fabComment.setOnClickListener {
            if(commentFragment != null){
                commentFragment?.show(childFragmentManager.beginTransaction(), commentFragment?.javaClass?.name)
            } else {
                commentFragment = CommentFragment.newInstance(article?.comments!!)
                commentFragment?.show(childFragmentManager.beginTransaction(), commentFragment?.javaClass?.name)
            }
        }
    }

    /** 上划按钮点击时整体回到最顶部  */
    private fun initFabBack() {
        binding.fabBackToTop.visibility = View.INVISIBLE
        binding.fabBackToTop.setOnClickListener {
            if(binding.fabBackToTop.visibility == View.VISIBLE){
                binding.nestedScrollView.fullScroll(View.FOCUS_UP)
                binding.layoutAppBar.setExpanded(true)
            }
        }
    }

    /**菜单的监听*/
    private fun initToolbar(){
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_edit -> {
                    //TODO:编辑操作
                    Snackbar.make(requireContext(), binding.root, "编辑", Snackbar.LENGTH_SHORT).show()
                    return@setOnMenuItemClickListener true
                }
                R.id.menu_item_copy -> {
                    //TODO: 复制操作
                    Snackbar.make(requireContext(), binding.root, "复制成功", Snackbar.LENGTH_SHORT).show()

                    return@setOnMenuItemClickListener true
                }
                R.id.menu_item_delete -> {
                    //TODO: 删除文件操作
                    Snackbar.make(requireContext(), binding.root, "删除", Snackbar.LENGTH_SHORT).show()
                    return@setOnMenuItemClickListener true
                }
                R.id.menu_item_more->{
                    return@setOnMenuItemClickListener false
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initTagRecyclerView(){
        tagAdapter = TagAdapter(requireContext()).apply {
            this.updateData(article?.tags!!)
        }
        tagAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(holder: RecyclerView.ViewHolder, position: Int) {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    val tag = tagAdapter.getData(position)
                    add(R.id.container, TagRefFragment.newInstance(tag.tagID, tag.tag))
                    addToBackStack("tag_ref")
                    commit()
                }
            }
        })

        binding.recyclerViewTags.adapter = tagAdapter

        binding.recyclerViewTags.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
    }


    /**设置全屏*/
    private fun setFullScreen(isFullScreen: Boolean){
        this.isFullScreen = isFullScreen
        when(isFullScreen){
            true->{
                binding.layoutAppBar.setExpanded(false)
                if(binding.fabBackToTop.visibility == View.VISIBLE){
                    fabUpCollapseAnimatorSet.start()
                }
                if(binding.fabComment.visibility == View.VISIBLE){
                    fabComCollapseAnimatorSet.start()
                }
            }
            false->{
                binding.layoutAppBar.setExpanded(true)
                if (binding.fabComment.visibility == View.INVISIBLE) {
                    fabComExpandAnimatorSet.start()
                }
                if(binding.fabBackToTop.visibility == View.INVISIBLE && binding.nestedScrollView.scrollY != 0){
                    fabUpExpandAnimatorSet.start()
                }
            }
        }

    }

}