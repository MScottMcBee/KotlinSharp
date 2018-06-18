package com.mscottmcbee.kotlinsharp.parsing

import com.mscottmcbee.kotlinsharp.Token

//https://joshuagrams.github.io/pep/
class Parser(var tokens: List<Token>, var grammar: Grammar) {

   // var S:Item? = null

    var S:ArrayList<Set> = ArrayList()


    fun parse(startRule: Rule) {

        var newSet = Set(grammar, 0)
        var startItem = Item(startRule.firstSymbol,newSet)
        newSet.items.add(startItem)
       // set.

        //var set0: Set = //process(predict(Set(grammar, 0), startRule.firstSymbol))
        S.add(newSet)

        var i = 1;
        while (i < S.size){
            var j = 0
            while (j < S[i].items.size) {
                //println("\t\t\t"+S[i].items.size)
                var item = S[i].items[j]

                var tag:GrammarSymbol = item.tag

                if (tag is LR0) {
                    var next =  nextSymbol(tag)
                    predict(S[i],next!!)
                } else {
                    complete(item)
                }
                j++
            }
            i++
        }



        /*var setN: Set = tokens.fold(set0, { set: Set, symbol: GrammarSymbol ->

            println("!!" + set + " " + symbol)

            process(scan(set, symbol))
        })*/

       // var x:Item? = setN.idx[startRule.firstSymbol]!![set0]

        var g = 231 + 2

    }

    fun predict(set: Set, symbol: GrammarSymbol): Set {
        if (symbol is LR0){
            println("aaaa")
        }
        if (symbol is Nonterminal){
            var productions = grammar.getAllProductionsForNonterminal(symbol)
            println("predict "+symbol);
            if (productions != null){
                for (rule in productions!!) {
                    addItem(LR0(rule, 0), set, set)
                }
            }
        }
        return set
    }


    //TODO: Complete is only called on tokens?????
    fun complete(completed: Item) {
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

    fun scan(set: Set, symbol: GrammarSymbol): Set {
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

    fun addItem(tag: GrammarSymbol, start: Set): Item {
        for (i:Item in start.items){
            if i ==
        }
       // end.idx[tag]?.also { dict ->
        //    if (dict[start.position] != null)
        //        return dict[start.position]!!
       // }
        return appendItem(tag, start)*/
    }

    fun appendItem(tag: GrammarSymbol, start: Set, end: Set): Item {
      /*  var newItem = Item(tag, start, end)
        end.items.add(newItem)
        end.idxAdd(tag, start, newItem)
        if (tag is LR0) {
            end.wantsAdd(tag.rule.firstSymbol, newItem)
        }
        return newItem*/
    }

    fun nextSymbol(lr0: LR0): GrammarSymbol? {
        if (lr0.dot < lr0.rule.production.size)
            return lr0.rule.production[lr0.dot]
        return lr0.rule.firstSymbol
    }

    fun advance(symbol: GrammarSymbol): GrammarSymbol {
        if (symbol is LR0) {
            var lr0 = symbol
            println ( "" + lr0.rule.firstSymbol)
            if (lr0.dot == lr0.rule.production.size - 1) {
                return LR0(lr0.rule, lr0.dot + 1)
            }
            return lr0.rule.firstSymbol
        }else{
            var x = 12;
        }
        return symbol
    }

    // Derivations

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
    }
}