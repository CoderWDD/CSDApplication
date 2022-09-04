package com.example.article.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
import com.example.article.data.entity.Folder
import com.example.article.data.entity.relationship.folders.Folder2BaseArticle
import com.example.article.data.entity.relationship.folders.Folder2ChildFolder

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Dao
interface FolderDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insert(vararg folders: Folder)

    @Update
     suspend fun update(folder: Folder): Int

    @Delete
     suspend fun delete(folder: Folder)

    @Query("DELETE FROM folder")
     suspend fun deleteAll()

    @Query("SELECT * FROM folder WHERE folderID = :folderID")
     suspend fun getFolder(folderID: Int): Folder?

//    @Query("SELECT * FROM folder WHERE parentFolderID = :parentID ORDER BY folder_name")
//    suspend suspend fun getChildFolders(parentID: Int): List<Folder>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM folder WHERE folderID = :folderID")
    suspend fun getChildFolder(folderID: Int): Folder2ChildFolder

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM folder WHERE folderID = :folderID")
    suspend fun getChildBaseArticle(folderID: Int): Folder2BaseArticle

    @Query("SELECT * FROM folder WHERE folder_name LIKE '%'||:query||'%'")
    suspend fun searchFolderByQuery(query: String): List<Folder>
}
