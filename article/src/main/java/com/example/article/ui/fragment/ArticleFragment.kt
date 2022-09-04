package com.example.article.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.SearchManager
import android.content.Context
import android.content.Context.SEARCH_SERVICE
import android.content.DialogInterface
import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.SearchRecentSuggestions
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.BaseAdapter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withCreated
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.article.AppHelper
import com.example.article.R
import com.example.article.constants.Constants
import com.example.article.data.entity.Article
import com.example.article.data.remote.doError
import com.example.article.data.remote.doSuccess
import com.example.article.databinding.FragmentArticleBinding
import com.example.article.ext.ViewModelFactory
import com.example.article.ext.getArticleDialog
import com.example.article.module.RepositoryModule
import com.example.article.ui.adapter.ResultAdapter
import com.example.article.ui.adapter.TagAdapter
import com.example.article.ui.adapter.ViewPagerAdapter
import com.example.article.ui.base.BaseFragment
import com.example.article.ui.inter.OnItemClickListener
import com.example.article.ui.inter.OnItemLongClickListener
import com.example.article.ui.viewModel.ArticleViewModel
import com.example.article.utils.LocalStorageUtil
import com.example.article.utils.NetWorkUtils
import com.example.article.utils.SpUtil
import com.example.article.utils.ToastUtil
import com.example.network.apollo.ApolloClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/09
 *     desc   : ArticleFragment
 *     version: 1.0
 * </pre>
 */
@OptIn(FlowPreview::class)
class ArticleFragment : BaseFragment() {
    //viewModel
    private val viewModel: ArticleViewModel by
        activityViewModels {
            ViewModelFactory(requireActivity().application, RepositoryModule.provideArticleRepository(
            ApolloClient.apollo, LocalStorageUtil.dataBase))
        }

    private lateinit var binding: FragmentArticleBinding

    private val tabList = listOf("推荐", "移动开发", "WEB开发", "游戏开发", "人工智能")

    private val fragmentList = listOf(
        RecommendFragment(),
        PathFragment.newInstance(Constants.Android),
        PathFragment.newInstance(Constants.Web),
        PathFragment.newInstance(Constants.Game),
        PathFragment.newInstance(Constants.AI)
    )

    private lateinit var searchDialog: AlertDialog

    private lateinit var resultDialog: AlertDialog

    private lateinit var resultAdapter: ResultAdapter

    private lateinit var articleInfoDialog: AlertDialog

    private lateinit var tagsAdapter: TagAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //带参数的fragment
        }
        //初始化AppHelper
        AppHelper.init(context = requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //viewBinding
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        //设置Toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        initViewPager()
        initSwipeRefreshLayout()
        initSearchDialog()
        initResultDialog()
        initArticleInfoDialog()

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!NetWorkUtils.isConnected(requireContext())) {
            Snackbar.make(binding.root, "网络连接失败" , Snackbar.LENGTH_INDEFINITE).show()
        }
        //开始构建文件树
        viewModel.buildTree()

        //获取是否在刷新
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.isRefreshingStateFlow.collect{ isRefreshing ->
                    binding.swipeRefreshLayoutMain.isRefreshing = isRefreshing
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //注册网络监听
        registerNetworkStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
        //取消网络监听
        unRegisterNetworkStatus()
    }
    /**
     * 显示overflowMenu的图标
     *  */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_article_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
        //使overflowMenu显示图标和文字
        if (menu.javaClass.simpleName == "MenuBuilder") {
            try {
                val method = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", java.lang.Boolean.TYPE)
                method.isAccessible = true
                method.invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /** 对menu的监听 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //搜索dialog
            R.id.search->{
                searchDialog.show()
                return true
            }
            //刷新
            R.id.refresh->{
                if(!binding.swipeRefreshLayoutMain.isRefreshing){
                    binding.swipeRefreshLayoutMain.isRefreshing = true
                    //少于1分钟中刷新达咩
                    if(System.currentTimeMillis() - viewModel.lastUpdateTime < 60_000L){
                        Snackbar.make(binding.root, "刚刚已经刷新辽~请稍后再试~", Snackbar.LENGTH_SHORT).show()
                        binding.swipeRefreshLayoutMain.isRefreshing = false
                    }else {
                        viewModel.buildTree()
                    }
                }
                return true
            }
            //退出
            R.id.exit->{
                //退出App
                AlertDialog.Builder(requireContext()).apply{
                    setTitle("请确定要退出?")
                    setCancelable(true)
                    setNegativeButton("取消") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("确定") { dialog, _ ->
                        dialog.dismiss()
                        requireActivity().finish()
                    }
                    show()
                }
                return true
            }
            else->return super.onOptionsItemSelected(item)
        }
    }

    /** tabLayout绑定viewPager2 */
    private fun initViewPager(){
        //viewPager预加载
        binding.viewPager.offscreenPageLimit = fragmentList.size

        binding.viewPager.adapter = ViewPagerAdapter(fragmentList, childFragmentManager, requireActivity().lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position->
            tab.text = tabList[position]
        }.attach()

        //初始化时将当前tab进行缩放
        val animator1 = ObjectAnimator.ofFloat(binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)?.view, "scaleX", 1f, 1.3f)
        val animator2 = ObjectAnimator.ofFloat(binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)?.view, "scaleY", 1f, 1.3f)
        AnimatorSet().apply {
            duration = 300
            play(animator1).with(animator2)
            start()
        }

        //tab切换的动画
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                val animatorX = ObjectAnimator.ofFloat(tab.view, "scaleX", 1f, 1.3f)
                val animatorY = ObjectAnimator.ofFloat(tab.view, "scaleY", 1f, 1.3f)
                AnimatorSet().apply {
                    duration = 300
                    play(animatorX).with(animatorY)
                    start()
                }
                //记录当前fragment， 与Constant的常量所对应
                viewModel.curItem = binding.tabLayout.selectedTabPosition - 1

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val animatorX = ObjectAnimator.ofFloat(tab.view, "scaleX", 1.3f, 1f)
                val animatorY = ObjectAnimator.ofFloat(tab.view, "scaleY", 1.3f, 1f)
                AnimatorSet().apply {
                    duration = 300
                    play(animatorX).with(animatorY)
                    start()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })
    }

    /**
     * 初始化一个搜索用的dialog
     *
     * */
    private fun initSearchDialog(){
        val view = layoutInflater.inflate(R.layout.dialog_search, null)
        val searchView: SearchView = view.findViewById(R.id.dialog_searchView)

        searchDialog = AlertDialog.Builder(requireContext())
            .setTitle("搜索")
            .setView(view)
            .setPositiveButton("搜索"){_,_->
                searchView.setQuery(searchView.query, true)
            }.setNegativeButton("取消"){dialog,_->
                dialog.dismiss()
            }
            .create()

        //由于 [getButton]方法只能在调用 [show()] 后才能获取，否则会报空指针
        searchDialog.show()
        searchDialog.dismiss()

        val bt_search = searchDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val color = bt_search.currentTextColor
        bt_search.focusable = View.NOT_FOCUSABLE
        bt_search.isClickable = false
        bt_search.setTextColor(Color.parseColor("#D3D3D3"))

        searchView.findViewById<AutoCompleteTextView>(androidx.appcompat.R.id.search_src_text).threshold = 1


        lifecycleScope.launch(Dispatchers.IO){
            val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
            val to = intArrayOf(R.id.tv_item_history)
            val suggestions = viewModel.fetchAllArticleTitle()


            withContext(Dispatchers.Main) {
                val cursorAdapter = SimpleCursorAdapter(context, R.layout.item_history, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)

                searchView.suggestionsAdapter = cursorAdapter

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query != null) {
                            //从数据库中找
                            viewModel.searchBaseArticlesByQuery(query)
                            resultDialog.show()
                            searchDialog.dismiss()
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        //搜索框内文字为空时搜索按钮不能点击
                        if (newText.isNullOrEmpty()) {
                            bt_search.focusable = View.NOT_FOCUSABLE
                            bt_search.isClickable = false
                            bt_search.setTextColor(Color.parseColor("#D3D3D3"))
                        } else {
                            bt_search.focusable = View.FOCUSABLE
                            bt_search.isClickable = true
                            bt_search.setTextColor(color)
                        }

                        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                        newText?.let {
                            suggestions.forEachIndexed { index, suggestion ->
                                if (suggestion.contains(newText, true))
                                    cursor.addRow(arrayOf(index, suggestion))
                            }
                        }

                        cursorAdapter.changeCursor(cursor)
                        return false
                    }
                })
            }
        }

        searchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                //隐藏
                val inputMethodManager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchView.setQuery(selection, false)

                // Do something with selection
                return true
            }
        })



    }

    /**
     * 初始化搜索结果的dialog
     * */
    private fun initResultDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_search_result, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.dialog_recyclerView)
        val progressBar = view.findViewById<ProgressBar>(R.id.dialog_progressbar)
        resultAdapter = ResultAdapter(requireContext())

        recyclerView.adapter = resultAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        resultDialog = AlertDialog
            .Builder(requireContext())
            .setView(view)
            .setCancelable(false)
            .setTitle("搜索结果")
            .setPositiveButton("确定"){ _, _ ->
                resultDialog.show()
            }
            .create()
        //实现对viewModel的返回结果的观察
        viewModel.searchResult.observe(requireActivity()){
            resultAdapter.submitData(requireActivity().lifecycle, it)
            progressBar.visibility = View.INVISIBLE
        }

        //对结果的监听
        resultAdapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(holder: RecyclerView.ViewHolder, position: Int) {
                resultAdapter.getBaseArticle(position)?.let {
                    resultDialog.dismiss()
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        add(R.id.container, ContentFragment.newInstance(it.baseArticleID, it.path))
                        addToBackStack("content")
                        commit()
                    }
                }?: run{
                    ToastUtil.makeText(requireContext(), "加载失败~")
                }
            }
        })

        //长按显示文章信息
        resultAdapter.setOnItemLongClickListener(object : OnItemLongClickListener{
            override fun onItemLongClick(holder: RecyclerView.ViewHolder, position: Int) {
                resultAdapter.getBaseArticle(position)?.let {
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

    }

    /**
     * 初始化显示文章信息的dialog
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
                    resultDialog.dismiss()
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
     * 显示用于显示文章信息的dialog
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


    /**
     * 下拉刷新的初始化
     * - 解决滑动冲突的问题
     * - 下拉刷新的监听
     * */
    private fun initSwipeRefreshLayout() {
        //解决viewpager2 和 swipeRefreshLayout的滑动冲突
        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageScrollStateChanged(state: Int) {
                //viewPager2没有任何动作时（当只有1个Fragment的情况xu）
                if(state == ViewPager2.SCROLL_STATE_IDLE){
                    binding.swipeRefreshLayoutMain.isEnabled = true//设置可触发
                }
                //viewPager正在拖动
                else if(state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    if(!binding.swipeRefreshLayoutMain.isRefreshing) {
                        binding.swipeRefreshLayoutMain.isEnabled = false//设置不可触发
                    }
                    //viewPager正在稳定，没有拖动
                }else if(state == ViewPager2.SCROLL_STATE_SETTLING){
                    binding.swipeRefreshLayoutMain.isEnabled = true//设置可触发
                }
            }
        })

        binding.swipeRefreshLayoutMain.setOnRefreshListener {
            if(binding.swipeRefreshLayoutMain.isRefreshing){
                //少于三分钟刷新达咩
                if(System.currentTimeMillis() - viewModel.lastUpdateTime < 180_000L){
                    Snackbar.make(binding.root, "刚刚已经刷新辽~ 请稍后再试~ ", Snackbar.LENGTH_SHORT).show()
                    binding.swipeRefreshLayoutMain.isRefreshing = false
                }else if(binding.tabLayout.selectedTabPosition == 0){
                    viewModel.setRecommendRefresh(true)
                    lifecycleScope.launch(Dispatchers.IO){
                        delay(300)
                        binding.swipeRefreshLayoutMain.isRefreshing = false
                    }
                }else if(binding.tabLayout.selectedTabPosition != 0){
                    viewModel.buildTree()
                }
            }
        }
    }

    /**
     * 注册网络状态监听
     *
     * 在[onResume]调用
     */
    private fun registerNetworkStatus() {
        val networkService = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkService.registerDefaultNetworkCallback(networkCallback)
        } else {
            networkService.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
        }
    }

    /**
     * 取消注册
     *
     * 在 [onPause] 或 [onDestroy] 取消
     * */
    private fun unRegisterNetworkStatus() {
        val networkService = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkService.unregisterNetworkCallback(networkCallback)
    }

    /**
     * 监听回调
     * */
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val bar = Snackbar.make(binding.root, "网络似乎已经断开了呢~", Snackbar.LENGTH_INDEFINITE)
            if(!NetWorkUtils.isConnected(requireContext())){
                bar.show()
            }else{
                bar.dismiss()
            }
        }
    }
}