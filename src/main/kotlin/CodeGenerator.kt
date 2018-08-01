package com.mscottmcbee.kotlinsharp

import com.mscottmcbee.kotlinsharp.parsing.TreeNode
import com.mscottmcbee.kotlinsharp.semantics.AST
import com.mscottmcbee.kotlinsharp.semantics.ASTNode
import com.mscottmcbee.kotlinsharp.semantics.SymbolTable
//import com.mscottmcbee.kotlinsharp.semantics.VarNode
import semantics.IdentifierData

class CodeGenerator() {


    var typeStack: ArrayList<String> = ArrayList()

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

        for (identData in SymbolTable.scopes[0].values){
            result += "${identData.type} ${identData.name},\n"
        }
        result = result.dropLast(2)
        result += ")\n"
        return result
    }

    private fun recCodeGen(code: String, node: Any): String {
        var c = code
        when (node.javaClass) {
            ASTNode::class.java -> {
                var n = node as ASTNode
                for (childNode in n.children) {
                    c = recCodeGen(c, childNode)
                }
                when (n.type) {
                    "ADD" -> {
                        c += "add \n"
                    }
                    "MUL" -> {
                        c += "mul \n"
                    }
                    "PRINT" -> {
                       c += "call void [mscorlib]System.Console::WriteLine(${typeStack.removeAt(typeStack.count()-1)})\n"
                    }
                    "DEC" -> {
                        c += "stloc ${(n.metaNode!! as IdentifierData).name}\n"
                        typeStack.removeAt(typeStack.count()-1)
                    }
                    "SEQ"->{}
                    else -> {
                        println("Unhandled ast node type ${n.type}")
                    }
                }
            }
            Integer::class.java -> {
                typeStack.add("int32")
                c += "ldc.i4 $node\n"
            }
            String::class.java -> {
                typeStack.add("string")
                c += "ldstr \"$node\"\n"
            }
            TreeNode::class.java ->{
                val ident = ((node as TreeNode).data as Token).stringData!!
                val identData = SymbolTable.getSymbol(ident)
                typeStack.add(identData!!.type)
                c += "ldloc $ident\n"
            }

            else -> {
                println("Unknown AST Node type ${node.javaClass}")
            }

        }
        return c
    }
}