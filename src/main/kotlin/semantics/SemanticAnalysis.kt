package com.mscottmcbee.kotlinsharp.semantics

import com.mscottmcbee.kotlinsharp.Token
import com.mscottmcbee.kotlinsharp.TokenType
import com.mscottmcbee.kotlinsharp.parsing.TreeNode

class SemanticAnalysis() {

    fun analyze(root: TreeNode): AST {
        collapse(root)
        var astRoot = makeAST(root)

        var ast = AST()
        ast.root = astRoot as ASTNode

        var variables = ArrayList<VarNode>()
        getVariables(ast.root as ASTNode, variables)
        ast.variables = variables

        return ast
    }


    private fun collapse(node: TreeNode) {
        for (i in 0 until node.children.size) {
            while (node.children[i].children.size == 1) {
                node.children[i] = node.children[i].children[0]
            }
            collapse(node.children[i])
        }
    }

    private fun makeAST(node: TreeNode): Any? {
        var thisASTNode: ASTNode? = null
        var thisNotNode: Any? = null
        if (node.item == null) {
            if (node.data != null && node.data is Token) {
                var token = node.data
                when (token.type) {
                    TokenType.NUMBER -> {
                        thisNotNode = token.numericValue ?: token.floatValue
                    }
                    TokenType.IDENTIFIER -> {
                        thisNotNode = VarNode(token.stringData!!, "int32")
                    }
                    TokenType.STRING -> {
                        thisNotNode = token.stringData
                    }
                    else -> {
                    }
                }
            }
        } else {
            when (node.item!!.rule.firstSymbol.id) {
                "START", "STATEMENTS" -> {
                    thisASTNode = ASTNode("SEQ")
                }
                "ADD" -> {
                    thisASTNode = ASTNode("ADD")
                }
                "MUL" -> {
                    thisASTNode = ASTNode("MUL")
                }
                "ASSIGNMENT" -> {
                    thisASTNode = ASTNode("ASS")
                }
                "PRINT" -> {
                    thisASTNode = ASTNode("PRINT")
                }
                "DECLARATION" -> {
                    thisASTNode = ASTNode("DEC")
                }
                else -> {
                    println("UNHANDLED SYMBOL ${node.item!!.rule.firstSymbol.id}")
                }
            }

            if (thisASTNode != null) {
                for (i in 0 until node.children.size) {
                    var result = makeAST(node.children[i])
                    if (result != null) {
                        thisASTNode.children.add(result)
                    }
                }
                when(thisASTNode.type){
                    "DEC" -> {
                        thisASTNode.metaNode = thisASTNode.children[0]
                        thisASTNode.children.removeAt(0)
                    }
                    else -> {
                    }
                }
            }




        }
        return thisASTNode ?: thisNotNode
    }

    private fun getVariables(node: Any, results: ArrayList<VarNode>) {
        when (node.javaClass) {
            ASTNode::class.java -> {
                var n = node as ASTNode
                for (childNode in n.children) {
                    getVariables(childNode, results)
                }
            }
            VarNode::class.java -> {
                var vNode = node as VarNode
                if (!results.contains(vNode)) {
                    results.add(vNode)
                }
            }
            else -> {
            }
        }
    }


}

