package com.mscottmcbee.kotlinsharp.parsing


data class Item(val tag: GrammarSymbol, val start:Set): Derivation(null, null, null){

/*
    fun isFinished():Boolean {
        return prodIndex >= rule.production.count()-1
    }

    fun getNextSymbol():GrammarSymbol{
        return rule.production[prodIndex+1]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (rule != other.rule)
            return false
        if (prodIndex != other.prodIndex)
            return false
        if (originIndex != other.originIndex)
            return false

        return true
    }
    */
}