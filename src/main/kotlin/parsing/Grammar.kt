package com.mscottmcbee.kotlinsharp.parsing

class Grammar{

    var productions: ArrayList<Nonterminal> = ArrayList()

    fun getAllProductionsForNonterminal( string:String): ArrayList<Rule>?{
        for (nonterm in productions){
            if (string == nonterm.id){
                return nonterm.rules
            }
        }
        return null
    }

    fun getAllProductionsForNonterminal( nonterminal: Nonterminal): ArrayList<Rule>? {
        return getAllProductionsForNonterminal(nonterminal.id)
    }

}