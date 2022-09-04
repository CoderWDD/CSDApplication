package com.example.article.data.local

import androidx.room.TypeConverter
import com.example.article.data.entity.Comment
import com.example.article.data.entity.Tag
import com.example.article.ui.base.gsonInstance
import com.google.gson.reflect.TypeToken

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class TagListConverter {
    @TypeConverter
    fun revert(json: String): List<Tag>? {
        try {
            val type = object : TypeToken<List<Tag>>(){ }.type
            return gsonInstance.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    @TypeConverter
    fun convert(list: List<Tag>): String{
        return gsonInstance.toJson(list)
    }
}
class CommentListConverter{
    @TypeConverter
    fun revert(json: String): List<Comment>? {
        try {
            val type = object : TypeToken<List<Comment>>(){ }.type
            return gsonInstance.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    @TypeConverter
    fun convert(list: List<Comment>): String{
        return gsonInstance.toJson(list)
    }
}

class IntListConverter{
    @TypeConverter
    fun revert(json: String): List<Int>? {
        try {
            val type = object : TypeToken<List<Int>>(){ }.type
            return gsonInstance.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    @TypeConverter
    fun convert(list: List<Int>): String{
        return gsonInstance.toJson(list)
    }
}