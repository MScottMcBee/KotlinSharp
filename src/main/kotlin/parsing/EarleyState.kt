package com.mscottmcbee.kotlinsharp.parsing

import sun.awt.Symbol

data class EarleyState(val production: Production, var prodIndex:Int, val originIndex:Int){

    fun isFinished():Boolean {
        return prodIndex == production.symbols.count()-1
    }

    fun getNextSymbol():GrammarSymbol{
        return production.symbols[prodIndex]
    }

}