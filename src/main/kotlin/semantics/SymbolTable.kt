package com.mscottmcbee.kotlinsharp.semantics

import semantics.IdentifierData

class SymbolTable{

    companion object {
        var scopes:ArrayList<HashMap<String,IdentifierData>> = ArrayList()
        private var writeIndex = 0
        private var readIndex = 0


        fun start(){
            enterScope()
        }

        fun enterScope(){
            scopes.add(HashMap())
        }

        fun addItem(ident:String){
            if (scopes[writeIndex].containsKey(ident)){
                println("ERROR, THIS THING: $ident, IS ALREADY DEFINED")
            }
            scopes[writeIndex][ident] = IdentifierData(ident)
        }

        fun specifyType(ident:String, type:String ){
            scopes[writeIndex][ident]!!.type = type
        }

        fun getSymbol(ident:String):IdentifierData?{
            return scopes[writeIndex][ident]
        }
    }
}