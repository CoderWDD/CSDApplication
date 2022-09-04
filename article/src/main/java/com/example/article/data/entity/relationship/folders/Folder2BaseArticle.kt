package com.example.article.data.entity.relationship.folders

import androidx.room.Relation
import com.example.article.data.entity.BaseArticle

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
data class Folder2BaseArticle (
    val folderID: Int,
    @Relation(parentColumn = "folderID", entityColumn = "belongFolderID")
    var baseArticles : MutableList<BaseArticle>,
)