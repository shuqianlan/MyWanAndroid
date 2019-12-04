package com.xihu.mywanandroid.net.beans

public data class Article(
//    val audit:Int,
    val author:String,
    val shareUser:String,
//    val chapterId:Int,
    val chapterName:String,
    val collect:Boolean,
//    val courseId:Int,
//    val desc:String,
//    val id:Long,
    val niceDate: String,
    val publishTime:Long,
//    val shareDate:Long,
//    val superChapterId:Int,
    val superChapterName:String,
    val title:String,
//    val type:Int,
    val zan:Int,
    val fresh:Boolean,
    val tags:List<Tags>
) {

    data class Tags(
        val name:String,
        val url:String
    )

    public fun getOwnerName():String {
        return if (!author.isNullOrEmpty()) author else shareUser
    }

    public fun getFreshText():String {
        return if (fresh) "新" else ""
    }

    public fun getTagText():String {
        return if (!tags.isEmpty()) tags[0].name else ""
    }

    public fun lastDate() = niceDate

    public fun category():String {
        var result = ""
        if (!superChapterName.isNullOrEmpty()) {
            result += superChapterName
        }

        if (!chapterName.isNullOrEmpty()) {
            result += chapterName
        }

        return result
    }

}