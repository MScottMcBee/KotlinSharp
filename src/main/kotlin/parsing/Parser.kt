package com.mscottmcbee.kotlinsharp.parsing

import com.mscottmcbee.kotlinsharp.Token

class Parser(var tokens: List<Token>) {

    var states: Array<ArrayList<EarleyState>?> = arrayOfNulls<ArrayList<EarleyState>?>(tokens.size + 1)

    fun parse(startProduction: Production) {
        addState(EarleyState(startProduction,0,0),0)

        for (i in 0 .. states.size ){
            if (states[i] != null) {
                var stateSet = states[i]!!
                for (j in 0..stateSet.size) {
                    if (!stateSet[j].isFinished()){
                        var symbol = stateSet[j].getNextSymbol()
                        if(symbol is Token){
                            scanner()
                        }else{
                            predictor()
                        }
                    }else{
                        completer()
                    }
                }
            }
        }
//return chart
    }

    private fun addState(state:EarleyState, index:Int){
        if (states[index] == null){
            states[index] = ArrayList()
        }
        states[index]?.add(state)
    }

    private fun scanner(){

    }

    private fun predictor(){

    }

    private fun completer(){

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