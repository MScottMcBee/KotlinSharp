package com.mscottmcbee.kotlinsharp.parsing


class LR0(var rule:Rule, var dot:Int) : GrammarSymbol(){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LR0

        if (rule != other.rule) return false
        if (dot != other.dot) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rule.hashCode()
        result = 31 * result + dot
        return result
    }
}