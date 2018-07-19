package com.mscottmcbee.kotlinsharp

class Tokenizer {

    var index = 0
    lateinit var charArray: CharArray
    lateinit var stringBuilder: StringBuilder

    var tokens = ArrayList<Token>()

    var row = 0
    var col = 0

    fun tokenize(file: String) : List<Token> {

        charArray = file.toCharArray()

        while (index < charArray.size) {
            val token = getNextToken()
            if (token != null) {
                tokens.add(token)
            }
        }
        return tokens
    }

    private fun getNextToken(): Token? {
        return when {
            charArray[index] == ';' -> {
                index++
                col++
                Token(TokenType.SEMICOLON)
            }

            charArray[index].isLetter() -> {
                stringBuilder = StringBuilder()
                stringBuilder.append(charArray[index])
                index++
                col++
                proccessIdentifier()
            }

            charArray[index].isDigit() -> {
                stringBuilder = StringBuilder()
                stringBuilder.append(charArray[index])
                index++
                col++
                proccessNumber()
            }

            charArray[index] == '=' -> {
                index++
                col++
                Token(TokenType.ASSIGNMENT)
            }

            charArray[index] == '+' -> {
                index++
                col++
                Token(TokenType.ADD)
            }

            charArray[index] == '*' -> {
                index++
                col++
                Token(TokenType.MUL)
            }

            charArray[index] == '(' -> {
                index++
                col++
                Token(TokenType.OPENPAREN)
            }

            charArray[index] == ')' -> {
                index++
                col++
                Token(TokenType.CLOSEPAREN)
            }

            charArray[index] == '"' -> {
                stringBuilder = StringBuilder()
                index++
                col++
                proccessString()
            }

            else -> {
                var xxx = charArray[index]
                if (!xxx.isWhitespace()){
                    println("unknown symbol at line ${row+1}, character ${col+1}")
                }
                if (xxx == '\n'){
                    col = 0
                    row++
                }else{
                    col++
                }
                index++
                null
            }
        }
    }

    private fun proccessIdentifier(): Token? {
        return when {
            charArray[index].isLetterOrDigit() || charArray[index] == '_' -> {
                stringBuilder.append(charArray[index])
                index++
                col++
                proccessIdentifier()
            }

            else -> {
                resolveIdentifier()
            }
        }
    }

    private fun resolveIdentifier(): Token {
        val identifier = stringBuilder.toString()
        return when (identifier) {
            "var" -> Token(TokenType.VAR)
            "do" -> Token(TokenType.DO)
            "end" -> Token(TokenType.END)
            "for" -> Token(TokenType.FOR)
            "print" -> Token(TokenType.PRINT)
            "read_int" -> Token(TokenType.READINT)
            "to" -> Token(TokenType.TO)
            else -> Token(TokenType.IDENTIFIER,identifier)
        }
    }

    private fun proccessNumber(): Token? {
        return when {
            charArray[index].isDigit()-> {
                stringBuilder.append(charArray[index])
                index++
                col++
                proccessNumber()
            }

            else -> {
                val numString = stringBuilder.toString()
                Token(TokenType.NUMBER, numString.toInt())
            }
        }
    }

    private fun proccessString(): Token? {
        return when {
            charArray[index] == '"'-> {
                index++
                col++
                val string = stringBuilder.toString()
                Token(TokenType.STRING, string)
            }

            else -> {
                stringBuilder.append(charArray[index])
                index++
                col++
                proccessString()
            }
        }
    }
}