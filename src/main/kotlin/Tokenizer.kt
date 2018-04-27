package com.mscottmcbee.kotlinsharp

class Tokenizer {

    fun MakeThoseTokensTony(file: String){


        var index = 0


        val charArray = file.toCharArray()

        while ( index < charArray.size){
            when{
                charArray[index] == ';' -> {
                    println("AAAA")
                    index++
                }

                charArray[index].isLetter() || charArray[index] == '_' -> {
                    val stringBuilder =
                }


                else -> {
                    index++
                }
            }

        }

    }


}