package com.mscottmcbee.kotlinsharp.parsing

class Production(){


    var symbols:ArrayList<GrammarSymbol> = ArrayList()

    val firstSymbol:GrammarSymbol
        get() = symbols[0]



}