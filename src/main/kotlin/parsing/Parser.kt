package com.mscottmcbee.kotlinsharp.parsing

import com.mscottmcbee.kotlinsharp.Token

class Parser(private val tokens:List<Token>, private val data:ArrayList<ArrayList<Item>>){


    fun parse(startRule: Rule):TreeNode?{
        var start: Item? = null

        for (item: Item in data[0]) {
            if (item.rule == startRule && item.nextSymbol() == null && item.setIndex == data.size - 1) {
                start = item
                break
            }
        }

        if (start != null) {
            var node = search(0, start,0)
            return node!!
        }
        return null
    }


    //search returns the list of items that match the item production inbetween startingIndex and item.setIndex
    // This does a weird thing where it decides if something is valid partly here, partly in validate
    private fun search(startingIndex: Int, item: Item, w:Int): TreeNode? {
        var node = TreeNode(item,ArrayList(),null)

        var y = startingIndex
        for (i in 0 until item.rule.production.size) {
            var symbol = item.rule.production[i]

            if (symbol is Nonterminal) {
                for (newItem: Item in data[y]) {
                    if (newItem.rule.firstSymbol == symbol) {
                        if (item.rule.production.size == i+1) {
                            if (newItem.setIndex == item.setIndex) {
                                var newNode = search(y, newItem, w+1)
                                if (newNode == null) {
                                    println("Broke")
                                }
                                node.children.add(newNode!!)
                                y = newItem.setIndex
                            }
                        }else {
                            var b = validate(newItem.setIndex, item, i+1, item.setIndex)
                            if (b) {
                                var newNode = search(y, newItem, w+1)
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
                node.children.add(TreeNode(null, ArrayList(),tokens[y]))
                y += 1
            }
        }
        return node
    }

    private fun validate(startingIndex:Int, item: Item, depth:Int, target:Int):Boolean{
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
            for (newItem : Item in possibles){
                if (depth == item.rule.production.size - 1) {
                    if (newItem.setIndex == target){
                        found = true
                        break
                    }
                }else{
                    if (validate(newItem.setIndex,item,depth +1, target)){
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
                    return validate(startingIndex + 1, item, depth + 1, target)
                }
            }
        }
        return false
    }

}