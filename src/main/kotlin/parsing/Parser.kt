package com.mscottmcbee.kotlinsharp.parsing

import com.mscottmcbee.kotlinsharp.Token

// http://loup-vaillant.fr/tutorials/earley-parsing/recogniser
class Parser(var tokens: List<Token>, var grammar: Grammar) {


    var S:ArrayList<Set> = ArrayList()


    fun parse(startRule: Rule) {

        var newSet = Set()
        var startItem = Item(startRule,0,0)
        newSet.items.add(startItem)
       // set.

        //var set0: Set = //process(predict(Set(grammar, 0), startRule.firstSymbol))
        S.add(newSet)

        var i = 0;
        while (i < S.size){
            println("Set # " + i )
            var j = 0
            while (j < S[i].items.size) {
                //println("\t\t\t"+S[i].items.size)
                var item = S[i].items[j]
                var nextSymbol = item.nextSymbol()

                if (nextSymbol == null) {
                    println("complete")
                    complete(item,i)
                }else {
                    var symbol = nextSymbol!!
                    if (symbol is Nonterminal) {
                        predict(symbol, i)
                    }else if (symbol is Token){
                        scan(symbol, i,item)
                    }else {
                        println("Whatthe fuck???")
                    }
                }



               /* var tag:GrammarSymbol = item.tag

                if (tag is LR0) {
                    var next =  nextSymbol(tag)
                    predict(S[i],next!!)
                } else {
                    complete(item)
                }
                */
                j++
            }
            println("End Set# $i of ${S.size}")
            i++
        }



        /*var setN: Set = tokens.fold(set0, { set: Set, symbol: GrammarSymbol ->

            println("!!" + set + " " + symbol)

            process(scan(set, symbol))
        })*/

       // var x:Item? = setN.idx[startRule.firstSymbol]!![set0]

        var found = false
        for (item:Item in S[S.size-1].items){
            if (item.rule == startRule && item.nextSymbol() == null && item.setIndex == 0){
                found = true
                println("Finished Successfully!")
                break
            }
        }
        if (!found){
            println("Could not parse :(")
        }

        var g = 231 + 2

    }

    fun predict(nonterminal: Nonterminal, setIndex: Int) {
        var productions = grammar.getAllProductionsForNonterminal(nonterminal)
        println("predict "+nonterminal);
        if (productions != null){
            for (rule in productions!!) {
                addItem(rule,setIndex)
            }
        }
    }


    //TODO: Complete is only called on tokens?????
    fun complete(completed: Item, currentSetIndex:Int ) {
        println("Completed ${completed.rule.firstSymbol}")
        for (item: Item in S[completed.setIndex].items){
            if (item.nextSymbol() == completed.rule.firstSymbol){
                var newItem = Item(item.rule,item.dot+1, item.setIndex)
                S[currentSetIndex].items.add(newItem)
            }
        }



      /*  println("Completed " + completed)
        if (completed.start.wants[completed.tag] != null) {
            for (item in completed.start.wants[completed.tag]!!) {
                //if (item.tag is LR0) {
                    var advancedSymbol = advance(item.tag)
                    var a:Item = addItem(advancedSymbol, item.start, completed.end)
                    addDerivation(a, item, completed)
               // }
            }
        }else{println("null?")}*/
    }

    fun scan(grammarToken: Token, setIndex:Int, item:Item) {

        println("scanning found token " + grammarToken + " vs " + tokens[setIndex])
        if (grammarToken == tokens[setIndex]){
            println("It was there")
            if (S.size <= setIndex+1 ){
                var newSet = Set()
                S.add(newSet)
            }
            var newItem = Item(item.rule, item.dot+1,item.setIndex)

            S[setIndex+1].items.add(newItem)

        }else{
            println("wrong one")
        }


     /*   var newSet = Set(set.grammar, set.position + 1)
        println("new set "+newSet.position)
        S.add(newSet)
        addItem(symbol, set, newSet)
        return newSet*/



    }

  /*  fun processOnce(set: Set) {
//        try {
            var i = 0
            while (i < set.items.size) {
                println("\t\t\t"+set.items.size)
                var item = set.items[i]



                var tag:GrammarSymbol = item.tag



                if (tag is LR0) {
                    var next =  nextSymbol(tag)
                    predict(set,next!!)
                } else {
                    complete(item)
                }
                i++
            }
    //    }catch (e:Exception){
      //      println(e)
       // }
    }*/

   /* fun process(set: Set): Set {
      //  do {
      //      var old = set.items.size
            processOnce(set)
           // println(""+ set.items.size + " " + old)
     //   } while (set.items.size > old)

        return set
    }*/

    fun addItem(rule: Rule, setIndex: Int) {

        var newItem = Item(rule, 0, setIndex)


        for (item:Item in S[setIndex].items){
            if (newItem == item){
                return
            }
        }
        S[setIndex].items.add(newItem)
    }

    fun advance(symbol: GrammarSymbol) {
      /*  if (symbol is LR0) {
            var lr0 = symbol
            println ( "" + lr0.rule.firstSymbol)
            if (lr0.dot == lr0.rule.production.size - 1) {
                return LR0(lr0.rule, lr0.dot + 1)
            }
            return lr0.rule.firstSymbol
        }else{
            var x = 12;
        }
        return symbol*/
    }

    // Derivations
/*
    fun addDerivation(item: Derivation, left: Item?, right: Item?){
        var leftt:Derivation? = left
        if (left == null && right == null)
            return

        if (left != null && left!!.tag is LR0 && (left!!.tag as LR0).dot <= 1){
            leftt = leftt!!.right
        }

        if (item.left == null && item.right == null){
            setDerivation(item, leftt, right)
        }else if (item.right != null){
            addSecondDerivation(item, leftt, right)
        }else{
            addAnotherDerivation(item, left, right)
        }
    }

    fun setDerivation(item: Derivation, left: Derivation?, right: Derivation?){
        item.left = left
        item.right = right
    }

    fun sameDerivation(item: Derivation, left: Derivation?, right: Derivation?): Boolean{
        return item.left == left && item.right == right
    }

    fun addSecondDerivation(item: Derivation, left: Derivation?, right: Derivation?){
        if (!sameDerivation(item,left,right)){
            var old = Derivation(item.left,item.right,null)
            item.left = Derivation(left,right,old)
            item.right = null
        }
    }

    fun addAnotherDerivation(item: Derivation, left: Derivation?, right: Derivation?){
        var d:Derivation? = item.left
        while(d != null){

            if (d == null || sameDerivation(d!!,left,right)){
                return
            }
            d=d.next
        }
        item.left = Derivation(left,right,item.left)
    }*/
}