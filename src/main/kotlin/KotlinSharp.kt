package com.mscottmcbee.kotlinsharp

import java.io.*

class KotlinSharp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Kotlin# 0.0.1")
            var fileLocation = "C:\\Users\\Scott\\Documents\\KotlinSharp\\build\\libs\\abc.xyz"
            if (args.size > 0){
                fileLocation = "./$args[0]"
               /// println("Please pass a file to build")
            }
                println("\"building\" " + fileLocation)
                val kotlinSharp = KotlinSharp()
                kotlinSharp.build(fileLocation)

        }
    }

    fun build(fileLocation: String){
        var file = File(fileLocation)
        var reader = FileReader(file)
        var s = reader.readText()

        val tokenizer = Tokenizer()

        tokenizer.MakeThoseTokensTony(s)
    }

}