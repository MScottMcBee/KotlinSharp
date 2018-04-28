class Token(val type: TokenType) {

    constructor(type: TokenType, data: String) : this(type){
        stringData = data
    }

    constructor(type: TokenType, data: Int) : this(type){
        numericValue = data
    }

    constructor(type: TokenType, data: Float) : this(type){
        floatValue = data
    }

    var stringData: String? = null
    var numericValue: Int? = null
    var floatValue: Float? = null



}

enum class TokenType{
    IDENTIFIER,
    SEMICOLON,
    STRING,
    NUMBER,
    VAR,
    EQUALS,
    ASSIGNMENT,
    READINT,
    PRINT,
    FOR,
    TO,
    DO,
    END
}