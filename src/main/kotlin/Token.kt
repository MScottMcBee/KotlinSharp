package com.mscottmcbee.kotlinsharp

import com.mscottmcbee.kotlinsharp.parsing.GrammarSymbol

class Token(val type: TokenType): GrammarSymbol() {

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