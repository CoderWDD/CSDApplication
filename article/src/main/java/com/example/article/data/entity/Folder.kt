package com.example.article.data.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.article.data.entity.relationship.folders.Folder2BaseArticle
import com.example.article.data.entity.relationship.folders.Folder2ChildFolder

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/25
 *     desc   : 文件树结点
 *     version: 1.0
 * </pre>
 */
@Entity
data class Folder(
    @PrimaryKey val folderID: Int,
    @ColumnInfo(name = "folder_name") override val title: String,
    @ColumnInfo(name = "folder_path") var path: String,
    val parentFolderID: Int
): IFile {
    @Ignore
    var childFolders: Folder2ChildFolder = Folder2ChildFolder(folderID, mutableListOf())

    @Ignore
    var baseArticles: Folder2BaseArticle = Folder2BaseArticle(folderID, mutableListOf())

    //存baseArticle的id
    var baseArticleIDs = mutableListOf<Int>()

    @Ignore
    var parent: Folder? = null

    companion object{
        val differCallback = object : DiffUtil.ItemCallback<Folder>(){
            override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
                return oldItem.folderID == newItem.folderID
            }

            override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
                return oldItem == newItem
            }
        }
    }

}

