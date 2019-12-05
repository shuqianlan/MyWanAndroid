package com.xihu.huidefeng.net.api

import com.xihu.huidefeng.net.beans.*
import com.xihu.mywanandroid.net.beans.Banner
import com.xihu.mywanandroid.net.beans.ListArticles
import com.xihu.mywanandroid.net.beans.TopArticle
import retrofit2.http.*

interface ApiService {

    @GET("banner/json")
    suspend fun topBanners():ResponseData<List<Banner>>

    // 置顶
    @GET("article/top/json")
    suspend fun topArticles():ResponseData<List<TopArticle>>

    // 分页数据
    @GET("article/list/{page}/json")
    suspend fun articles(@Path("page") page:Int):ResponseData<ListArticles>


}