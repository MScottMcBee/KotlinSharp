class Token(val type: TokenType, val data: String) {

}

enum class TokenType{
    IDENTIFIER,
    SEMICOLON,
    STRING,
    NUMBER
}