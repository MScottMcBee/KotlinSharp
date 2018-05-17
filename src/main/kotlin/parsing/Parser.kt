package com.mscottmcbee.kotlinsharp.parsing

import com.mscottmcbee.kotlinsharp.Token

class Parser(var tokens: List<Token>, var grammar: Grammar) {

    var states: Array<ArrayList<Item>?> = arrayOfNulls<ArrayList<Item>?>(tokens.size+1)
    var completed: HashMap<Production,ArrayList<Item>?>  = HashMap()


    var sProd: Production? = null

    var final: Item? = null

    fun parse(startProduction: Production) {
        addState(Item(startProduction,0,0),0)

        sProd = startProduction

        for (i in 0 .. states.size-1 ){
            if (states[i] != null) {
                var stateSet = states[i]!!
                var j = 0
                while( j < stateSet.size) {
                    var item = stateSet[j]
                    if (!item.isFinished()){
                        var symbol = item.getNextSymbol()
                        if(symbol is Token){
                            scanner(item, i)
                        }else{
                            predictor(item, i)
                        }
                    }else{
                        completer(item, i)
                    }
                    j++
                }

            }
        }
        var g = 231+2;
//return chart
    }

    private fun addState(item:Item, index:Int){
        if (states[index] == null){
            states[index] = ArrayList()
        }
        states[index]?.add(item)
    }

    private fun predictor(item: Item, k: Int){
        var next = item.getNextSymbol() as Nonterminal
        for (production in next.productions){

            var newState = Item(production,0,item.originIndex)
            if (!states[k]!!.contains(newState)){
                addState(newState,k)
            }
        }
        var x = 112+23
    }

    private fun scanner(item: Item, k: Int){
        try {
            if (item.getNextSymbol() == tokens[k]) {
                var newState = Item(item.production, item.prodIndex + 1, item.originIndex)
                newState.predPointer.add(item)
                addState(newState, k + 1)
            }
        }catch (e:Exception){
            var asd = 4356
        }
    }

    private fun completer(completedItem: Item, k: Int){

        if (completedItem.originIndex == k) {

            if (completed[completedItem.production] == null){
                completed[completedItem.production] = ArrayList()
            }
            completed[completedItem.production]?.add(completedItem)
        }

        if (completedItem.production == sProd){
            final = completedItem
        }



        if (states[completedItem.originIndex] != null) {
            for (item in states[completedItem.originIndex]!!){
                if (item.getNextSymbol() == completedItem.production.firstSymbol){
                    Item(item.production,item.prodIndex+1,item.originIndex).also {
                      it.reducPointer.add(completedItem)
                      it.predPointer.add(item)
                      addState(it,k)
                    }
                }
            }
        }
    }


    /*
    procedure PREDICTOR((A → α•Bβ, j), k, grammar)
    for each (B → γ) in GRAMMAR-RULES-FOR(B, grammar) do
        ADD-TO-SET((B → •γ, k), S[k])
    end

procedure SCANNER((A → α•aβ, j), k, words)
    if a ⊂ PARTS-OF-SPEECH(words[k]) then
        ADD-TO-SET((A → αa•β, j), S[k+1])
    end

procedure COMPLETER((B → γ•, x), k)
    for each (A → α•Bβ, j) in S[x] do
        ADD-TO-SET((A → αB•β, j), S[k])
    end
     */


}