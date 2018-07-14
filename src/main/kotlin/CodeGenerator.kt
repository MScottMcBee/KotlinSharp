package com.mscottmcbee.kotlinsharp

import com.mscottmcbee.kotlinsharp.parsing.TreeNode
import com.mscottmcbee.kotlinsharp.semantics.AST
import com.mscottmcbee.kotlinsharp.semantics.ASTNode
import com.mscottmcbee.kotlinsharp.semantics.VarNode

class CodeGenerator() {

    fun codegen(ast: AST): String {
        var s = """
.assembly extern mscorlib {}
.assembly Hello {}
.module Hello.exe

.class Hello.Program
extends [mscorlib]System.Object
{
    .method static void Main(string[] args)
    cil managed
    {
        .entrypoint
        """
        s += locals(ast)
        s += recCodeGen("", ast.root!!)
        s += """

        ret
    }
}"""
        return s
    }

    private fun locals(ast: AST): String {
        var result = ".locals init ("
        for (i in 0 until ast.variables!!.size){
            var varNode = ast.variables!![i]
            result += "${varNode.type} ${varNode.id}"
            if (i < ast.variables!!.size - 1){
                result += ","
            }
        }
        result += ")\n"
        return result
    }

    private fun recCodeGen(code: String, node: Any): String {
        var c = code
        when (node.javaClass) {
            ASTNode::class.java -> {
                var n = node as ASTNode
                for (childNode in n.children) {
                    // if (childNode is ASTNode) {
                    c = recCodeGen(c, childNode)
                    //}
                }
                when (n.type) {
                    "ADD" -> {
                        c += "add \n"
                    }
                    "MUL" -> {
                        c += "mul \n"
                    }
                    "PRINT" -> {
                       c += "call void [mscorlib]System.Console::WriteLine(int32)\n"
                    }
                    "DEC" -> {
                        c += "stloc ${(n.metaNode!! as VarNode).id}\n"
                    }
                    else -> {
                        println("Unhandled ast node type ${n.type}")
                    }
                }
            }
            Integer::class.java -> {
                c += "ldc.i4 $node\n"
            }
            VarNode::class.java -> {
                c += "ldloc ${(node as VarNode).id}\n"
            }

            else -> {
                println("Unknown AST Node type ${node.javaClass}")
            }

        }
        return c
    }
}