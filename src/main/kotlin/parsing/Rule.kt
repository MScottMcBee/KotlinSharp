package com.mscottmcbee.kotlinsharp.parsing

import com.mscottmcbee.kotlinsharp.Token

class Rule{

    lateinit var firstSymbol: Nonterminal

    var production:ArrayList<GrammarSymbol> = ArrayList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rule

        if (other.production.size != production.size){
            return false
        }

        if (firstSymbol != other.firstSymbol)
            return false

        for (i in 0..production.size-1){
            if (production[i].javaClass != other.production[i].javaClass){
                return false
            }

            if (production[i] is Token){
                val thisToken = production[i] as Token
                val otherToken = other.production[i] as Token

                if (thisToken.type != otherToken.type){
                    return false
                }
            }

            if (production[i] is Nonterminal){
                val thisNonterminal = production[i] as Nonterminal
                val otherNonterminal = other.production[i] as Nonterminal

                if (thisNonterminal.id != otherNonterminal.id){
                    return false
                }
            }

        }

        return true
    }

    override fun hashCode(): Int {
        return firstSymbol.hashCode()+production.hashCode()
    }


}