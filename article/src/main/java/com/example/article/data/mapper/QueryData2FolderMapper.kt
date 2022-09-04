package com.example.article.data.mapper

import com.example.article.data.entity.BaseArticle
import com.example.article.data.entity.Folder
import com.example.article.data.entity.IFile
import com.example.model.FolderTreeQuery

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class QueryData2FolderMapper {

    /**
     * 转换为Folder
     * @param input 服务器返回的数据，注意不要为空
     * */
    fun map(input: FolderTreeQuery.Data, folder: Folder){
        input.pages?.tree?.forEach {
            if (it != null) {
                if (it.isFolder) {
                    folder.childFolders.folders.add(
                        Folder(
                            folderID = it.id,
                            title = it.title,
                            path = it.path,
                            parentFolderID = folder.folderID
                        )
                    )
                } else {
                    //文章信息
                    folder.baseArticleIDs.add(it.pageId?:it.id)
                }
            }


        }
    }

}