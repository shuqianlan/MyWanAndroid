package com.xihu.mywanandroid

import android.app.Application
import com.google.gson.Gson
import com.xihu.mywanandroid.net.beans.ConfigBean
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder

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


    }
}