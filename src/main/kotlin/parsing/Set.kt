package com.mscottmcbee.kotlinsharp.parsing

class Set(){

    var items: ArrayList<Item> = ArrayList()
   // var idx:HashMap<GrammarSymbol, HashMap<Int,Item>> = HashMap()
   // var wants:HashMap<GrammarSymbol, ArrayList<Item>> = HashMap()

  /*  fun idxAdd(tag:GrammarSymbol, start:Set, item:Item){
        if(idx[tag] == null) {
            idx[tag] = HashMap()
        }
        idx[tag]?.also{it[start.position]=item}
    }

    fun wantsAdd(symbol: GrammarSymbol, item: Item){
        if (wants[symbol] == null){
            wants[symbol] = ArrayList()
        }
        wants[symbol]?.add(item)
    }*/

    // Are wants added? Completed never seems to work on non-terminals. Does this make sense?

}