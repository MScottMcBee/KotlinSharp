package com.mscottmcbee.kotlinsharp.parsing


data class Item(val rule: Rule, val dot: Int, val setIndex:Int): Derivation(null, null, null){

    var start = -1

    fun nextSymbol(): GrammarSymbol? {
        if (dot < rule.production.size)
            return rule.production[dot]
        return null
    }

    override fun toString(): String {
        var s:String = "${rule.firstSymbol} -> "
        for (symbol:GrammarSymbol in rule.production){
            s += "$symbol "
        }
        s += ": dot: $dot, setIndex: $setIndex, start: $start"
        return s
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (rule != other.rule)
            return false
        if (dot != other.dot)
            return false
        if (setIndex != other.setIndex)
            return false

        return true
    }

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