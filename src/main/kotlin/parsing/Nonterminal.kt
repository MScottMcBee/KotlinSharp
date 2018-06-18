package com.mscottmcbee.kotlinsharp.parsing

class Nonterminal(val id:String): GrammarSymbol(){

    var rules:ArrayList<Rule> = ArrayList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Nonterminal

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Nonterminal(id='$id')"
    }


}