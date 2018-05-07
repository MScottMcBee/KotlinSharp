package com.mscottmcbee.kotlinsharp.parsing

class Production(){


    var symbols:List<GrammarSymbol> = ArrayList<GrammarSymbol>()

    val firstSymbol:GrammarSymbol
        get() = symbols[0]



}