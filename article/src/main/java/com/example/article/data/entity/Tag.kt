package com.example.article.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Entity
data class Tag(
    @PrimaryKey
    val tagID: Int,
    val tag: String
)
