package com.xihu.mywanandroid

import android.app.Application
import com.google.gson.Gson
//import com.growingio.android.sdk.collection.Configuration
//import com.growingio.android.sdk.collection.GrowingIO
import com.xihu.mywanandroid.net.beans.ConfigBean
import java.io.BufferedReader
import java.io.InputStreamReader


class WanApp :Application() {

    override fun onCreate() {
        super.onCreate()

        val context = applicationContext
        val reader = InputStreamReader(context.assets.open("config.json"), "UTF-8")
        reader.read()
        val bufferedReader = BufferedReader(reader)
        val builder = StringBuilder()
        var line:String?
        try {
            do {
                line = bufferedReader.readLine()
                if (line != null) {
                    builder.append(line).append("\n")
                }
            } while (line != null)
            ConfigBean.instance = Gson().fromJson(builder.toString(), ConfigBean::class.java)
        } catch (exp: Exception) {
            exp.printStackTrace()
        }

//        GrowingIO.startWithConfiguration(
//            this, Configuration()
//                .trackAllFragments()
//                .setTestMode(BuildConfig.DEBUG)
//                .setDebugMode(BuildConfig.DEBUG)
//                .setChannel("XXX应用商店")
//        )


    }
}