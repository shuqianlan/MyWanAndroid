package com.xihu.mywanandroid.net.beans

data class ListArticles(
    val curPage:Int, // 当前第几页
    val offset: Int,
    val over:Boolean,  // 是否结束
    val pageCount:Int, // 总页数
    val size:Int,
    val total:Int,
    val datas:List<Article>
)