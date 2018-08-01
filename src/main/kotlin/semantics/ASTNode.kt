package com.mscottmcbee.kotlinsharp.semantics

data class ASTNode(val type:String){
    var children: ArrayList<Any> = ArrayList()
    var metaNode:Any? = false
}