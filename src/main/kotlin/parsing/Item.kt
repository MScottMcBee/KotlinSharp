package com.mscottmcbee.kotlinsharp.parsing

import parsing.SPPFNode

data class Item(val production: Production, var prodIndex:Int, val originIndex:Int){

    var sppfNode: SPPFNode? = null

    var reducPointer: ArrayList<Item?> = ArrayList()

    var predPointer: ArrayList<Item?> = ArrayList()

    var threePointer: ArrayList<Item?> = ArrayList()

    fun isFinished():Boolean {
        return prodIndex >= production.symbols.count()-1
    }

    fun getNextSymbol():GrammarSymbol{
        return production.symbols[prodIndex+1]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (production != other.production)
            return false
        if (prodIndex != other.prodIndex)
            return false
        if (originIndex != other.originIndex)
            return false

        return true
    }
}