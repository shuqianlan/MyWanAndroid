package com.xihu.mywanandroid.net.beans

data class Project(
    var children:List<Any>?=null,
    var courseId:Int=0,
    var id:Int=-1,
    var name:String="",
    var order:Long=0,
    var parentChapterId:Int=0,
    var userControlSetTop:Boolean=false,
    var visible:Int=0
)