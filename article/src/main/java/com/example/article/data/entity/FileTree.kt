package com.example.article.data.entity


/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */

/**根部*/
class FileTree(var root: Folder?){

    fun getAndroid() = root?.childFolders?.folders?.find {
        it.path.contains("移动开发")
    }

    fun getWeb() = root?.childFolders?.folders?.find {
        it.path.contains("Web")
    }

    fun getAI() = root?.childFolders?.folders?.find {
        it.path.contains("人工智能")
    }

    fun getGame() = root?.childFolders?.folders?.find {
        it.path.contains("游戏开发")
    }
}