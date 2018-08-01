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

        SymbolTable.start()

        buildSymbolTable((ast.root) as Any)
        asd(ast.root as Any)
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

    /* comment */
    private fun makeAST(node: TreeNode): Any? {
        var currentASTNode: ASTNode? = null
        var thisLeaf: Any? = null
        if (node.item == null) {
            if (node.data != null && node.data is Token) {
                var token = node.data
                when (token.type) {
                    TokenType.NUMBER -> {
                        thisLeaf = token.numericValue ?: token.floatValue
                    }
                    TokenType.IDENTIFIER -> {
                        thisLeaf = node
                    }
                    TokenType.STRING, TokenType.IDENTIFIER -> {
                        thisLeaf = token.stringData
                    }
                    else -> {
                    }
                }
            }
        } else {
            when (node.item!!.rule.firstSymbol.id) {
                "start", "statements" -> {
                    currentASTNode = ASTNode("SEQ")
                }
                "add" -> {
                    currentASTNode = ASTNode("ADD")
                }
                "mul" -> {
                    currentASTNode = ASTNode("MUL")
                }
                "assignment" -> {
                    currentASTNode = ASTNode("ASS")
                }
                "print" -> {
                    currentASTNode = ASTNode("PRINT")
                }
                "declaration" -> {
                    currentASTNode = ASTNode("DEC")
                }
                else -> {
                    println("UNHANDLED AST THING ${node.item!!.rule.firstSymbol.id}")
                }
            }

            if (currentASTNode != null) {
                for (i in 0 until node.children.size) {
                    var result = makeAST(node.children[i])
                    if (result != null) {
                        currentASTNode.children.add(result)
                    }
                }
                when(currentASTNode.type){
                    "DEC" -> {
                //        currentASTNode.metaNode = currentASTNode.children[0]
                 //       currentASTNode.children.removeAt(0)
                    }
                    else -> {
                    }
                }
            }
        }
        return currentASTNode ?: thisLeaf
    }

    fun buildSymbolTable(node: Any){
        when (node.javaClass) {
            ASTNode::class.java -> {
                var astNode:ASTNode = node as ASTNode
                when (astNode.type) {
                    "DEC" -> {
                        var ident:String = ((astNode.children[0] as TreeNode).data as Token).stringData!!
                        SymbolTable.addItem(ident)
                        var typeString = findType(astNode.children[1])
                        SymbolTable.specifyType(ident,typeString)
                    }
                    else -> {
                        for (childNode in astNode.children) {
                            buildSymbolTable(childNode)
                        }
                    }
                }
            }
            else -> {
            }
        }
    }


    private fun asd(node: Any) {
        when (node.javaClass) {
            ASTNode::class.java -> {
                var n = node as ASTNode
                if (n.type == "DEC"){
                    n.metaNode = SymbolTable.getSymbol(((node.children[0] as TreeNode).data as Token).stringData!!)!!
                    n.children = arrayListOf(n.children[1])
                    var x = 12;
                }else {
                    for (childNode in n.children) {
                        asd(childNode)
                    }
                }
            }
        }
    }

    fun findType(node: Any):String{
        when (node.javaClass) {
            Integer::class.java -> {
                return "int32"
            }
            TreeNode::class.java -> {
                var identData = SymbolTable.getSymbol(((node as TreeNode).data as Token).stringData!!)
                return identData?.type ?: "string"
                //return "string"
            }
            String::class.java -> {
                var identData = SymbolTable.getSymbol(node as String)
                return identData?.type ?: "string"
            }
            ASTNode::class.java -> {
                return findType((node as ASTNode).children[0])
            }
        }


        return ""
    }

}