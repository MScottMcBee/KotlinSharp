package com.mscottmcbee.kotlinsharp.parsing

class Nonterminal: GrammarSymbol(){

    var id:String = ""

    var productions:List<Production> = ArrayList<Production>()

}