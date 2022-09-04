package com.example.article.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.article.data.entity.Article
import com.example.article.data.entity.relationship.article_tags.BaseArticleTagCrossRef
import com.example.article.data.entity.BaseArticle
import com.example.article.data.entity.Comment
import com.example.article.data.entity.Folder
import com.example.article.data.entity.Tag

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/25
 *     desc   : 数据库
 *     version: 1.0
 * </pre>
 */
@Database(
    entities = [Article::class, BaseArticle::class, Folder::class, Tag::class, Comment::class, BaseArticleTagCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    TagListConverter::class,
    CommentListConverter::class,
    IntListConverter::class
)
abstract class ArticleDataBase: RoomDatabase(){

    abstract fun FolderDao(): FolderDao

    abstract fun ArticleDao(): ArticleDao

    abstract fun BaseArticleDao(): BaseArticleDao

    abstract fun TagDao(): TagDao

    abstract fun CommentDao(): CommentDao

    abstract fun ArticleTagCrossRefDao(): BaseArticleTagCrossRefDao
}