package com.xihu.huidefeng.net.api

import com.xihu.huidefeng.net.beans.*
import com.xihu.mywanandroid.net.beans.*
import retrofit2.http.*

interface ApiService {

    // --------------------------- Home --------------------------- \\
    @GET("banner/json")
    suspend fun topBanners():ResponseData<List<Banner>>

    @GET("article/top/json") // 置顶
    suspend fun topArticles():ResponseData<List<Article>>

    @GET("article/list/{page}/json") // 分页数据
    suspend fun articles(@Path("page") page:Int):ResponseData<ListArticles>

    @GET("article/friend/json") // 常用网站
    suspend fun hotWebsites():ResponseData<List<Website>>

    @GET("article/hotkey/json") // 热词
    suspend fun hotkeys():ResponseData<List<Hotkey>>

}