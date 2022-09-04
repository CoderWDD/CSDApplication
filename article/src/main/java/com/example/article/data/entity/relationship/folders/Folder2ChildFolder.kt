package com.example.article.data.entity.relationship.folders

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.example.article.data.entity.Article
import com.example.article.data.entity.BaseArticle
import com.example.article.data.entity.Folder

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
/**
 * 文件夹下面的子项
 * */
data class Folder2ChildFolder(
    val folderID: Int, //父级文件夹的ID
    @Relation(parentColumn = "folderID", entityColumn = "parentFolderID")
    var folders: MutableList<Folder>,
)
