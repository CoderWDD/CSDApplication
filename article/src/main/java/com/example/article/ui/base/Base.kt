package com.example.article.ui.base

import com.example.article.data.entity.Folder
import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.jvm.Throws

/**
 *     author : swk
 *     time   : 2022/08/19
 *     desc   :
 *     version: 1.0
 */

val gsonInstance = Gson()


/** 将utc时间改为北京时间 */
@Throws(ParseException::class)
fun timeTransform(time: String): String{
    //解析utc时间
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'").apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    //解析为本地时区
    val after = format.parse(time)
    format.applyPattern("yyyy-MM-dd-HH:mm:ss");
    //默认时区
    format.timeZone = TimeZone.getDefault();
    return format.format(after!!)
}

//
fun usernameToIcon(username: String): String{
    val pattern = Pattern.compile("[\u4e00-\u9fa5]")
    for(pos in username.indices){
        //如果第一个字符为中文，则返回
        if(pos < 3  && pattern.matcher(username[pos].toString()).matches()){
            return username[pos].toString()
        }
    }
    return username.substring(0..2)
}

val okClient = OkHttpClient()

/** 去除转移符 */
fun removeEscapeSymbol(content: String): String{
    content.replace("\\n", "\n")
    content.replace("\\\"", "\"")
    return content
}


fun renderToHtml(render: String, isRemoveEscapeSymbol: Boolean): String{
    return "<html><body>${if(isRemoveEscapeSymbol) removeEscapeSymbol(render) else render}</body></html>"
}


fun display(root: Folder){
        val queue = LinkedList<Folder>().apply {
            offer(root)
        }
        while (queue.isNotEmpty()) {
            val cur = queue.pop()
            println("\n>>>>>>>>>>>>>>>>>cur: \n>>>articleID:    ${cur.folderID}\n>>>title: ${cur.title}\n>>>path:  ${cur.path}\n\n>>>>>>>>>folders:")
            for (folder in cur.childFolders.folders) {
                println(">id:${folder.folderID} >title:${folder.title} >path:${folder.path} >parent:${folder.parentFolderID}")
                queue.offer(folder)
            }
            println("\n>>>>>>>>>baseArticles:")
            for (article in cur.baseArticles.baseArticles) {
                println(">${article.baseArticleID}\t>${article.updatedAt}\t>${article.path}")
            }
            println("--------------------\n")
        }
}
