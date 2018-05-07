package com.mscottmcbee.kotlinsharp.parsing

class Grammar{

    var productions: ArrayList<Nonterminal> = ArrayList()


    fun getAllProductionsForNonterminal( nonterminal: Nonterminal): ArrayList<Production>?{
        for (nonterm in productions){
            if (nonterminal.id == nonterm.id){
                return nonterm.productions
            }
        }
        return null
    }

}