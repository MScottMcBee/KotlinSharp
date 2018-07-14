package com.mscottmcbee.kotlinsharp.semantics

data class ASTNode(val type:String){
    val children: ArrayList<Any> = ArrayList()
    var metaNode:Any? = false
}