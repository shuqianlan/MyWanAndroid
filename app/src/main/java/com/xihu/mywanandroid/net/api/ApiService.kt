package com.xihu.mywanandroid.net.api

import com.xihu.mywanandroid.net.beans.*
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

    @GET("friend/json") // 常用网站
    suspend fun hotWebsites():ResponseData<List<Website>>

    @GET("hotkey/json") // 热词
    suspend fun hotkeys():ResponseData<List<Hotkey>>

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun searchKey(@Path("page") page:Int, @Field("k") word: String):ResponseData<ListArticles>

    @GET("project/tree/json")
    suspend fun projectTree(): ResponseData<List<Project>>

    @GET("project/list/{page}/json") // page从1开始
    suspend fun projectlist(@Path("page") page:Int, @Query("cid") cid:Int):ResponseData<ProjectItems>

    @GET("article/listproject/{page}/json")
    suspend fun lastestProjestList(@Path("page") page:Int):ResponseData<ProjectItems>
}