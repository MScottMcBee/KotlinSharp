package com.mscottmcbee.kotlinsharp

import com.mscottmcbee.kotlinsharp.parsing.TreeNode

class StaticAnalysis(){

    fun analyze(root: TreeNode){
        collapse(root)
    }


    private fun collapse(node: TreeNode){
        for (i in 0 until node.children.size){
            while (node.children[i].children.size == 1){
                node.children[i] = node.children[i].children[0]
            }
            collapse(node.children[i])
        }
    }


}

