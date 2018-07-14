package com.mscottmcbee.kotlinsharp

import com.mscottmcbee.kotlinsharp.parsing.*
import com.mscottmcbee.kotlinsharp.semantics.SemanticAnalysis
import java.io.*

class KotlinSharp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Kotlin# 0.0.1")
            var fileLocation = "C:\\Users\\Scott\\Documents\\KotlinSharp\\build\\libs\\abc.xyz"
            fileLocation = "abcd.xyz"
            if (args.size > 0) {
                fileLocation = "${args[0]}"
                /// println("Please pass a file to build")
            }
            println("\"building\" " + fileLocation)
            val kotlinSharp = KotlinSharp()
            kotlinSharp.build(fileLocation)

        }
    }

    fun build(fileLocation: String) {
        var file = File(fileLocation)
        var reader = FileReader(file)
        var input = reader.readText()

        val tokenizer = Tokenizer()

        val tokens = tokenizer.tokenize(input)

        var grammar: Grammar = ToyLang().getGrammar()
        var start: Rule = grammar.getAllProductionsForNonterminal("START")?.get(0)!!

        var recognizer: EarlyRecognizer = EarlyRecognizer(tokens, grammar)

        var earleyData = recognizer.recognize(start)
        if (earleyData != null) {
            var parser = Parser(tokens, earleyData)

            var node: TreeNode? = parser.parse(start)

            var analyzer: SemanticAnalysis = SemanticAnalysis()
            var ast = analyzer.analyze(node!!)

            var generator = CodeGenerator()
            var code = generator.codegen(ast!!)

            println(code)

            var outFile = File(fileLocation+".il")
            var writer = FileWriter(outFile)
            writer.write(code)
            writer.close()
            println("File Wrote")
        }else{
            println("Something went wrong?")
        }

        var x = 1 + 2
    }





}