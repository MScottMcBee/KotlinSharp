package com.mscottmcbee.kotlinsharp.parsing

class Grammar{

    var productions: HashMap<GrammarSymbol,ArrayList<Production>> = HashMap()


    fun getAllProductionsForNonterminal( nonterminal: Nonterminal): ArrayList<Production>?{
        return productions[nonterminal]
    }

}