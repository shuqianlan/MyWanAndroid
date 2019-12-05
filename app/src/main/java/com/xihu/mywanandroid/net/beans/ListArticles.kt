package com.xihu.mywanandroid.net.beans

class ListArticles(
    val curPage:Int,
    val offset: Int,
    val over:Boolean,
    val pageCount:Int,
    val size:Int,
    val total:Int,
    val datas:List<TopArticle>
)