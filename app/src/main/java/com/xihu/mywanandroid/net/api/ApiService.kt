package com.xihu.huidefeng.net.api

import com.xihu.huidefeng.net.beans.*
import com.xihu.mywanandroid.net.beans.Article
import retrofit2.http.*

interface ApiService {

    @GET("banner/json")
    suspend fun banner():ResponseData<Article>

    // 置顶
    @GET("article/top/json")
    suspend fun topArticles():ResponseData<List<Article>>

    // 分页数据
    @GET("article/list/{page}/json")
    suspend fun articles(@Path("page") page:Int):ResponseData<List<Article>>


}