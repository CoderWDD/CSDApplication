package com.example.article.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.article.constants.Constants
import com.example.article.data.entity.BaseArticle
import com.example.article.data.entity.FileTree
import com.example.article.data.entity.Folder
import com.example.article.data.entity.relationship.article_tags.Tag2BaseArticles
import com.example.article.data.remote.doSuccess
import com.example.article.data.repository.ArticleRepository
import com.example.article.utils.LocalStorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@FlowPreview
@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ArticleViewModel @Inject constructor(
    private val repository: ArticleRepository
): ViewModel() {
    private val TAG = "viewModel"

    //最近的更新时间
    var lastUpdateTime = Constants.UNINITIALIZED.toLong()

    //文件树
    private var fileTree = FileTree(null)
//    private val _fileTreeStateFlow = MutableStateFlow(fileTree)
//    val fileTreeStateFlow = _fileTreeStateFlow.asStateFlow()

    //查询结果的stateflow
    private val _searchResult = MutableStateFlow("")

    val searchResult = _searchResult
        .debounce(300)
        .filter {
            return@filter it.isNotEmpty()
        }
        .flatMapLatest { query->
            repository.fetchBaseArticleByQuery(query).cachedIn(viewModelScope)
        }
        .catch { cause: Throwable ->
            //TODO: 处理异常
        }.asLiveData()


    //刷新状态的显示
    private val _isRefreshingStateFlow = MutableStateFlow(false)
    val isRefreshingStateFlow = _isRefreshingStateFlow.asStateFlow()


    //刷新完毕后的状态
    private val _refreshState = MutableStateFlow("刷新成功")
    val refreshState = _refreshState.asStateFlow()


    /**
     * 构建文件树
     * */
    fun buildTree() = viewModelScope.launch(Dispatchers.IO){
        lastUpdateTime = System.currentTimeMillis()

        var rootFolder: Folder? = null
        repository
            .fetchFolder(0, "root", "", -1)
            .onStart {
                //设置更新状态
                _isRefreshingStateFlow.value = true
            }
            .onCompletion {
                //模拟网络速度慢
//                delay(10000)
                //构造完文件树后
                fileTree = FileTree(rootFolder)
//                rootFolder?.let { folder -> display(folder) }
                //更新路径数据
                updatePath()
                //更新完成：刷新取消
                _isRefreshingStateFlow.value = false
            }
            .collect{ response->
                response.doSuccess {
                    rootFolder = it
                    val queue = LinkedList<Folder>()
                    queue.offer(rootFolder)
                    while(queue.isNotEmpty()){
                        val cur = queue.pop()
                        with(cur.childFolders.folders) {
                            for (pos in this.indices){
                                repository.fetchFolder(
                                    folderID = this[pos].folderID,
                                    title = this[pos].title,
                                    path = this[pos].path,
                                    parentFolderID = cur.parentFolderID,
                                    this[pos]).collect() //传入 folder则无需处理
                                queue.offer(this[pos])
                            }
                        }
                    }
                }
            }
    }

    /**
     * 从本地搜索base
     * @param query 查询参数(仅限于标题)
     * */
    fun searchBaseArticlesByQuery(query: String){
        _searchResult.value = query
    }

    suspend fun fetchAllTags() = repository.fetchTags()


    suspend fun fetchTagWithBaseArticle(tagID: Int) = repository.fetchTagWithBaseArticles(tagID)


    suspend fun fetchAllArticleTitle() = LocalStorageUtil.dataBase.BaseArticleDao().getAllBaseArticleTitles()

//---------------------------------PathFragment-----------------------------------------

    /**判断ArticleFragment的Viewpager中的当前fragment*/
    var curItem = 0

    private val _androidFolder = MutableStateFlow(fileTree.getAndroid())
    private val androidFolder = _androidFolder.asStateFlow()

    private val _webFolder = MutableStateFlow(fileTree.getWeb())
    private val webFolder = _webFolder.asStateFlow()

    private val _gameFolder = MutableStateFlow(fileTree.getGame())
    private val gameFolder = _gameFolder.asStateFlow()

    private val _aiFolder = MutableStateFlow(fileTree.getAI())
    private val aiFolder = _aiFolder.asStateFlow()

    /**
     * 更新所有的路径， 当[buildTree]完成时调用此方法
     * */
    private fun updatePath(){
        _androidFolder.value = fileTree.getAndroid()
        _webFolder.value = fileTree.getWeb()
        _gameFolder.value = fileTree.getGame()
        _aiFolder.value = fileTree.getAI()
    }

    /**
     * 根据[tag]返回不同的路径 StateFlow
     * @param tag PathFragment所属的tag
     * */
    fun getFolderStateFlow(tag: Int) =
        when(tag){
            Constants.Android -> androidFolder
            Constants.Web -> webFolder
            Constants.Game -> gameFolder
            Constants.AI -> aiFolder
            else -> null
        }
    /**
     * 返回不同的 当前folder
     * */
    fun getFolder(tag: Int?) =
        when(tag){
            Constants.Android -> _androidFolder.value
            Constants.Web -> _webFolder.value
            Constants.Game -> _gameFolder.value
            Constants.AI -> _aiFolder.value
            else -> null
        }

    //点击文件夹时更新
    fun setNextFolder(tag: Int?, folderID: Int) {
        when (tag) {
            Constants.Android -> _androidFolder.value = getFolder(tag)?.childFolders?.folders?.find { it.folderID == folderID }
            Constants.Web -> _webFolder.value = getFolder(tag)?.childFolders?.folders?.find { it.folderID == folderID }
            Constants.Game -> _gameFolder.value = getFolder(tag)?.childFolders?.folders?.find { it.folderID == folderID }
            Constants.AI -> _aiFolder.value = getFolder(tag)?.childFolders?.folders?.find { it.folderID == folderID }
            else ->{}
        }
    }
    //返回上一级时更新
    fun setPreFolder(tag: Int?) {
        when (tag) {
            Constants.Android -> _androidFolder.value = getFolder(tag)?.parent
            Constants.Web -> _webFolder.value = getFolder(tag)?.parent
            Constants.Game -> _gameFolder.value = getFolder(tag)?.parent
            Constants.AI -> _aiFolder.value = getFolder(tag)?.parent
            else -> {}
        }
    }



//------------------------------------RecommendFragment-----------------------------------------------------




    /** 推荐文章的数据 */
    private val recommendTags = mutableListOf<Int>(7, 9)


    /**
     * 获取推荐的文章数据
     * TODO: 设置关键字
     * */
    fun fetchRecommendBaseArticle(): Flow<PagingData<BaseArticle>> {
        return repository.fetchRecommendBaseArticle(recommendTags).cachedIn(viewModelScope)
    }


    private val _recommendRefreshStateFlow = MutableStateFlow(false)
    val recommendRefreshStateFlow = _recommendRefreshStateFlow.asStateFlow()


    fun setRecommendRefresh(isRefreshing: Boolean){
        _recommendRefreshStateFlow.value = isRefreshing
    }

    //刷新完毕后所显示的文字
    private val _recommendRefreshOverStateFlow = MutableStateFlow("刷新成功")
    val recommendRefreshOverStateFlow = _recommendRefreshOverStateFlow.asStateFlow()

    fun setRecommendRefreshResult(isSuccess: Boolean){
        _recommendRefreshOverStateFlow.value = if(isSuccess) "刷新成功" else "刷新失败"
    }

    //---------------------------ContentFragment----------------------------


    /**
     * 获取文章
     * @param ID 文章的id
     * @param path 文章的路径
     * */
    suspend fun fetchArticle(ID: Int, path: String) = repository.fetchArticle(ID, path)



    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(Dispatchers.IO) {
            val articleDao = LocalStorageUtil.dataBase.ArticleDao()
            val baseArticleDao = LocalStorageUtil.dataBase.BaseArticleDao()
            val folderDao = LocalStorageUtil.dataBase.FolderDao()

            val queue = LinkedList<Folder?>()
            queue.offer(fileTree.root)
            while (queue.isNotEmpty()) {
                val cur = queue.pop()
                cur?.let {
                    folderDao.insert(cur)

                    for(x in cur.baseArticles.baseArticles){
                        baseArticleDao.insert(x)
                    }
                    //TODO: 看情况决定是否需要保存Article
                    for(x in cur.childFolders.folders){
                        queue.offer(x)
                    }
                }
            }
        }

        Log.d(TAG, "onCleared: >>>>>>>>>>>>>>>ViewModelCleared")

    }





}