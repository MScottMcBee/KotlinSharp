package com.mscottmcbee.kotlinsharp.parsing

class Grammar{

    var productions: ArrayList<Nonterminal> = ArrayList()

    fun getAllProductionsForNonterminal( string:String): ArrayList<Production>?{
        for (nonterm in productions){
            if (string == nonterm.id){
                return nonterm.productions
            }
        }
        return null
    }

    fun getAllProductionsForNonterminal( nonterminal: Nonterminal): ArrayList<Production>? {
        return getAllProductionsForNonterminal(nonterminal.id)
    }

}