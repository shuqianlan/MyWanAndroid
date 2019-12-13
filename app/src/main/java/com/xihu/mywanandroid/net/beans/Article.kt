package com.xihu.mywanandroid.net.beans

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
    val title:String,
//    val type:Int,
    var stick:Boolean=false,
    val zan:Int,
    val fresh:Boolean,
    val tags:List<Tags>
) {

    data class Tags(
        val name:String,
        val url:String
    )

}