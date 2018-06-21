package com.mscottmcbee.kotlinsharp.parsing

import com.mscottmcbee.kotlinsharp.Token

// http://loup-vaillant.fr/tutorials/earley-parsing/recogniser
class EarlyRecognizer(var tokens: List<Token>, var grammar: Grammar) {

    var S: ArrayList<Set> = ArrayList() //This is an awkward name, but what it's called in the literature a lot
    var outputData: ArrayList<ArrayList<Item>>? = null

    fun recognize(startRule: Rule): ArrayList<ArrayList<Item>>?{

        var newSet = Set()
        var startItem = Item(startRule, 0, 0)
        newSet.items.add(startItem)

        S.add(newSet)

        var i = 0;
        while (i < S.size) {
            var j = 0
            while (j < S[i].items.size) {
                var item = S[i].items[j]
                var nextSymbol = item.nextSymbol()

                if (nextSymbol == null) {
                    complete(item, i)
                } else {
                    var symbol = nextSymbol!!
                    if (symbol is Nonterminal) {
                        predict(symbol, i)
                    } else if (symbol is Token) {
                        scan(symbol, i, item)
                    }
                }
                j++
            }

            i++
        }

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
            return null
        }

        //Re order to make it easier to parse later
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

        var printout = false
        if (printout) {
            for (z in 0..outputData!!.size - 1) {
                println("=== $z ===")
                for (item in outputData!![z]) {
                    print(String.format("%-15s ->  ", item.rule.firstSymbol))
                    var asd: String = ""
                    for (symbol in item.rule.production) {
                        asd += symbol.toString() + " "
                    }
                    print(String.format("%-35s ->  ", asd))
                    print("(${item.start})->(${item.setIndex})")
                    println("")

                }
                println("")
            }
        }

        return outputData
    }

    private fun predict(nonterminal: Nonterminal, setIndex: Int) {
        var productions = grammar.getAllProductionsForNonterminal(nonterminal)
        if (productions != null) {
            for (rule in productions!!) {
                addItem(rule, setIndex)
            }
        }
    }

    private fun complete(completed: Item, currentSetIndex: Int) {
        for (item: Item in S[completed.setIndex].items) {
            if (item.nextSymbol() == completed.rule.firstSymbol) {
                var newItem = Item(item.rule, item.dot + 1, item.setIndex)
                S[currentSetIndex].items.add(newItem)
            }
        }
    }

    private fun scan(grammarToken: Token, setIndex: Int, item: Item) {
        if (grammarToken == tokens[setIndex]) {
            if (S.size <= setIndex + 1) {
                var newSet = Set()
                S.add(newSet)
            }
            var newItem = Item(item.rule, item.dot + 1, item.setIndex)
            S[setIndex + 1].items.add(newItem)
        }

    }

    private fun addItem(rule: Rule, setIndex: Int) {
        var newItem = Item(rule, 0, setIndex)
        for (item: Item in S[setIndex].items) {
            if (newItem == item) {
                return
            }
        }
        S[setIndex].items.add(newItem)
    }
}

