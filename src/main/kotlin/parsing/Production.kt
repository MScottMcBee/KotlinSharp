package com.mscottmcbee.kotlinsharp.parsing

import com.mscottmcbee.kotlinsharp.Token

class Production(){


    var symbols:ArrayList<GrammarSymbol> = ArrayList()

    val firstSymbol:GrammarSymbol
        get() = symbols[0]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Production

        if (other.symbols.size != symbols.size){
            return false
        }

        for (i in 0..symbols.size-1){
            if (symbols[i].javaClass != other.symbols[i].javaClass){
                return false
            }

            if (symbols[i] is Token){
                val thisToken = symbols[i] as Token
                val otherToken = other.symbols[i] as Token

                if (thisToken.type != otherToken.type){
                    return false
                }
            }

            if (symbols[i] is Nonterminal){
                val thisNonterminal = symbols[i] as Nonterminal
                val otherNonterminal = other.symbols[i] as Nonterminal

                if (thisNonterminal.id != otherNonterminal.id){
                    return false
                }
            }

        }

        return true
    }

    override fun hashCode(): Int {
        return symbols.hashCode()
    }


}