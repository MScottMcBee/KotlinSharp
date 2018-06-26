package com.mscottmcbee.kotlinsharp

import com.mscottmcbee.kotlinsharp.parsing.TreeNode

class CodeGenerator(){

    fun codegen(root: TreeNode): String {
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
        s = recCodeGen(s, root)
        s += """
        call void [mscorlib]System.Console::WriteLine(int32)
        ret
    }
}"""
        return s
    }

    private fun recCodeGen(code: String, node: TreeNode): String {
        var c = code
        for (node in node.children) {
            c = recCodeGen(c, node)
        }
        if (node.item != null) {
            when (node.item.rule.firstSymbol.id) {
                "ADD" -> {
                    c += "add \n"
                }
                "MUL" -> {
                    c += "mul \n"
                }
                else -> {
                }
            }
        } else if (node.data != null && node.data is Token) {
            var token = node.data as Token
            when (token.type) {
                TokenType.NUMBER -> {
                    c += "ldc.i4 ${token.numericValue} \n"
                }
                else -> {
                }
            }
        }
        return c
    }
}