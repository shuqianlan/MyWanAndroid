package com.xihu.huidefeng.net.beans

data class ResponseData<out T>(val errCode:Int, val errorMsg:String, val data:T)