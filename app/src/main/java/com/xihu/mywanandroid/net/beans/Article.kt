package com.xihu.mywanandroid.net.beans

import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.text.Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE
import androidx.core.text.HtmlCompat
import java.net.URLDecoder

data class Article(
//    val audit:Int,
    val author:String?=null,
    val shareUser:String?=null,
//    val chapterId:Int,
    val chapterName:String,
    val collect:Boolean,
//    val courseId:Int,
//    val desc:String,
    val id:Long,
    val niceDate: String,
    val publishTime:Long,
//    val shareDate:Long,
//    val superChapterId:Int,
    val superChapterName:String,
    var title:String,
//    val type:Int,
    val link:String,
    var stick:Boolean=false,
    val zan:Int,
    val fresh:Boolean,
    val tags:List<Tags>
) {

    data class Tags(
        val name:String,
        val url:String
    )

    init {
        println("before title $title")
        title = Html.fromHtml(title).toString()
        println("after title $title")

        author?.trim()
        shareUser?.trim()
    }
}