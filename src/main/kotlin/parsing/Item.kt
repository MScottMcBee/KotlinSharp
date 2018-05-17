package com.mscottmcbee.kotlinsharp.parsing

import sun.awt.Symbol

data class EarleyState(val production: Production, var prodIndex:Int, val originIndex:Int){

    fun isFinished():Boolean {
        return prodIndex >= production.symbols.count()-1
    }

    fun getNextSymbol():GrammarSymbol{
        return production.symbols[prodIndex+1]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EarleyState

        if (production != other.production)
            return false
        if (prodIndex != other.prodIndex)
            return false
        if (originIndex != other.originIndex)
            return false

        return true
    }
}