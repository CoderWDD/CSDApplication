package com.example.article.data.mapper

/**
 * <pre>
 *     author : 叶梦璃愁
 *     time   : 2022/08/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface Mapper<I, O> {
    suspend fun map(input: I): O

}