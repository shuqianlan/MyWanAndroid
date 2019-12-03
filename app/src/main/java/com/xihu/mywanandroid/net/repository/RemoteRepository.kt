package com.xihu.huidefeng.net.repository

import com.xihu.huidefeng.net.api.ApiService
import com.xihu.huidefeng.net.base.BaseRepository
import com.xihu.huidefeng.net.beans.ResponseData
import com.xihu.mywanandroid.net.beans.Article
import com.xihu.mywanandroid.net.beans.ConfigBean
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RemoteRepository private constructor(): BaseRepository() {
    private val retrofit:Retrofit
    private val apiService: ApiService

    init {
        val client = OkHttpClient.Builder()
            .readTimeout(ConfigBean.instance.Retrofit.readTimeout, TimeUnit.MILLISECONDS)
            .connectTimeout(ConfigBean.instance.Retrofit.connectTimeout, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor {
                val request = it.request()
                println("OkHttpClient intercept: content $request")
                it.proceed(request)
            }.build()

        retrofit = Retrofit.Builder()
            .baseUrl(ConfigBean.instance.Retrofit.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    companion object {
        val instance: RemoteRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RemoteRepository()
        }

    }

    suspend fun topArticles():ResponseData<List<Article>> = request {
        val response = apiService.topArticles()
        print("topArticles ========== $response")
        return@request response
    }

    suspend fun homeArticles(page:Int):ResponseData<List<Article>> = request {
        val response = apiService.articles(page)
        print("homeArticles ========== $response")
        return@request response
    }
}