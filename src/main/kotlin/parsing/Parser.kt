package com.mscottmcbee.kotlinsharp.parsing

import com.mscottmcbee.kotlinsharp.Token

// http://loup-vaillant.fr/tutorials/earley-parsing/recogniser
class Parser(var tokens: List<Token>, var grammar: Grammar) {


    var S: ArrayList<Set> = ArrayList()
    var outputData: ArrayList<ArrayList<Item>>? = null

    fun parse(startRule: Rule) {

        var newSet = Set()
        var startItem = Item(startRule, 0, 0)
        newSet.items.add(startItem)
        // set.

        //var set0: Set = //process(predict(Set(grammar, 0), startRule.firstSymbol))
        S.add(newSet)

        var i = 0;
        while (i < S.size) {
            println("Set # " + i)
            var j = 0
            while (j < S[i].items.size) {
                //println("\t\t\t"+S[i].items.size)
                var item = S[i].items[j]
                var nextSymbol = item.nextSymbol()

                if (nextSymbol == null) {
                    println("complete")
                    complete(item, i)
                } else {
                    var symbol = nextSymbol!!
                    if (symbol is Nonterminal) {
                        predict(symbol, i)
                    } else if (symbol is Token) {
                        scan(symbol, i, item)
                    } else {
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
        for (item: Item in S[S.size - 1].items) {
            if (item.rule == startRule && item.nextSymbol() == null && item.setIndex == 0) {
                found = true
                println("Finished Successfully!")
                break
            }
        }
        if (!found) {
            println("Could not parse :(")
            return
        }


        //Re order to make it easier to search or something
        outputData = ArrayList()

        for (c in 1..S.size) {
            outputData!!.add((ArrayList()))
        }

        i = 0
        while (i < S.size) {
            var j = 0
            while (j < S[i].items.size) {
                var item: Item = S[i].items[j]
                if (item.nextSymbol() == null) {
                    var newItem = Item(item.rule, item.dot, i)
                    newItem.start = item.setIndex
                    outputData!![item.setIndex].add(newItem)
                }
                j++
            }
            i++
        }

        // how do we get info out?

        for (z in 0..outputData!!.size-1){
            println("=== $z ===")
            for(item in outputData!![z]){
                print(String.format("%-15s ->  ", item.rule.firstSymbol))
                var asd:String = ""
                for (symbol in item.rule.production){
                    asd += symbol.toString()+" "
                }
                print (String.format("%-35s ->  ", asd))
                print ("(${item.start})->(${item.setIndex})")
                println("")

            }
            println("")
        }



        var start: Item? = null

        for (item: Item in outputData!![0]) {
            if (item.rule == startRule && item.nextSymbol() == null && item.setIndex == outputData!!.size - 1) {
                start = item
                break
            }
        }


        if (start != null) {
            var node = scan2(0, start,0)

            var g = 231 + 2
        }


        var g = 231 + 2

    }

    fun predict(nonterminal: Nonterminal, setIndex: Int) {
        var productions = grammar.getAllProductionsForNonterminal(nonterminal)
        println("predict " + nonterminal);
        if (productions != null) {
            for (rule in productions!!) {
                addItem(rule, setIndex)
            }
        }
    }


    //TODO: Complete is only called on tokens?????
    fun complete(completed: Item, currentSetIndex: Int) {
        println("Completed ${completed.rule.firstSymbol}")
        for (item: Item in S[completed.setIndex].items) {
            if (item.nextSymbol() == completed.rule.firstSymbol) {
                var newItem = Item(item.rule, item.dot + 1, item.setIndex)
                S[currentSetIndex].items.add(newItem)
            }
        }
    }

    fun scan(grammarToken: Token, setIndex: Int, item: Item) {

        println("scanning found token " + grammarToken + " vs " + tokens[setIndex])
        if (grammarToken == tokens[setIndex]) {
            println("It was there")
            if (S.size <= setIndex + 1) {
                var newSet = Set()
                S.add(newSet)
            }
            var newItem = Item(item.rule, item.dot + 1, item.setIndex)

            S[setIndex + 1].items.add(newItem)

        } else {
            println("wrong one")
        }

    }

    fun addItem(rule: Rule, setIndex: Int) {

        var newItem = Item(rule, 0, setIndex)


        for (item: Item in S[setIndex].items) {
            if (newItem == item) {
                return
            }
        }
        S[setIndex].items.add(newItem)
    }


    //scan2 returns the list of items that match the item production inbetween startingIndex and item.setIndex
    fun scan2(startingIndex: Int, item: Item,w:Int): TreeNode? {
        var data = outputData!!
        var index = 0
        var node = TreeNode(item,ArrayList(),null)
   //     println("SCAN $startingIndex ${item.setIndex}")

        var bool = scan3(startingIndex,item,0,item.setIndex)


        var possibles: ArrayList<Item> = ArrayList()

        var y = startingIndex
        for (i in 0 until item.rule.production.size) {
            var symbol = item.rule.production[i]
     //       println("?$w?? $symbol")

            if (symbol is Nonterminal) {
                var possibles: ArrayList<Item> = ArrayList()
                for (newItem: Item in data[y]) {
                    if (newItem.rule.firstSymbol == symbol) {
   //                     println("!$w!!" + newItem)
                        if (item.rule.production.size == i+1) {
                           // IS this the thing we NEED??????
                            if (newItem.setIndex == item.setIndex) {
                               // scan2(y, newItem, w + 1)

                                var newNode = scan2(y, newItem, w+1)
                                if (newNode == null) {
                                    println("Broke")
                                }
                                node.children.add(newNode!!)
                                y = newItem.setIndex
                            }
                        }else {

     //                       println("~~~~~~~~~")
                            var b = scan3(newItem.setIndex, item, i+1, item.setIndex)
    //                        println("$newItem,   $b")
                            if (b) {
                                var newNode = scan2(y, newItem, w+1)
                                if (newNode == null) {
                                    println("Broke")
                                }
                                node.children.add(newNode!!)
                                y = newItem.setIndex
                            }
                        }
                    }
                }


            } else if (symbol is Token) {
                println(tokens[y])
                node.children.add(TreeNode(null, ArrayList(),tokens[y]))
                y += 1
            }
        }
        var xxxx = 12
        return node
    }

    fun scan3(startingIndex:Int, item:Item, depth:Int, target:Int):Boolean{
     //   println("scan3: $startingIndex, $item, $depth, $target")
        var data = outputData!!
        if (startingIndex >= data.size){
            return false
        }
        var symbol = item.rule.production[depth]


        if (symbol is Nonterminal) {
            var possibles: ArrayList<Item> = ArrayList()
            for (newItem: Item in data[startingIndex]) {
                if (newItem.rule.firstSymbol == item.rule.production[depth]) {
                    possibles.add(newItem)
                }
            }

            var found = false
            for (newItem :Item in possibles){
                if (depth == item.rule.production.size - 1) {
                    if (newItem.setIndex == target){
                        found = true
                        break
                    }
                }else{
                    if (scan3(newItem.setIndex,item,depth +1, target)){
                        found = true
                        break
                    }
                }

            }
            return found

        }else if (symbol is Token){
            if (startingIndex >= tokens.size){
                return false
            }
            if (tokens[startingIndex] == symbol) {
                if (depth == item.rule.production.size - 1) {
                    return startingIndex + 1 == target
                } else {
                    return scan3(startingIndex + 1, item, depth + 1, target)
                }
            }
        }
        return false
    }


}


data class TreeNode(val item:Item?, val children:ArrayList<TreeNode>, val data:Any?)
