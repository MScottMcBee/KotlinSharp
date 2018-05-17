package com.mscottmcbee.kotlinsharp

class Tokenizer {

    var index = 0
    lateinit var charArray: CharArray
    lateinit var stringBuilder: StringBuilder

    var tokens = ArrayList<Token>()

    fun tokenizeMeCaptain(file: String) : List<Token> {

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
                Token(TokenType.SEMICOLON)
            }

            charArray[index].isLetter() -> {
                stringBuilder = StringBuilder()
                stringBuilder.append(charArray[index])
                index++
                proccessIdentifier()
            }

            charArray[index].isDigit() -> {
                stringBuilder = StringBuilder()
                stringBuilder.append(charArray[index])
                index++
                proccessNumber()
            }

            charArray[index] == '=' -> {
                index++
                Token(TokenType.ASSIGNMENT)
            }

            charArray[index] == '+' -> {
                index++
                Token(TokenType.ADD)
            }

            charArray[index] == '*' -> {
                index++
                Token(TokenType.MUL)
            }

            charArray[index] == '"' -> {
                stringBuilder = StringBuilder()
                index++
                proccessString()
            }

            else -> {
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
                val string = stringBuilder.toString()
                Token(TokenType.STRING, string)
            }

            else -> {
                stringBuilder.append(charArray[index])
                index++
                proccessString()
            }
        }
    }
}