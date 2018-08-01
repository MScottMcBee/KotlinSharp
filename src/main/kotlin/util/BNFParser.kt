package util

import com.mscottmcbee.kotlinsharp.Token
import com.mscottmcbee.kotlinsharp.TokenType
import com.mscottmcbee.kotlinsharp.parsing.Grammar
import com.mscottmcbee.kotlinsharp.parsing.Nonterminal
import com.mscottmcbee.kotlinsharp.parsing.Rule


//BNF because I'm too lazy to add EBNF support right now
class BNFParser {
    companion object {
        fun Generate(str: String): Grammar {
            var newGrammar = Grammar()

            var nonTerminalStrings = str.split(";")

            //prefill nonterminal list
            for (nonTerminalString in nonTerminalStrings) {
                if (nonTerminalString.trim().isNotEmpty()) {
                    var headAndBody = nonTerminalString.split("=")
                    var head = headAndBody[0].trim()
                    var nt = Nonterminal(head)
                    newGrammar.productions.add(nt)
                }
            }

            for (nonTerminalString in nonTerminalStrings) {
                if (nonTerminalString.trim().isNotEmpty()) {
                    var headAndBody = nonTerminalString.split("=")
                    var head = headAndBody[0].trim()
                    var body = headAndBody[1].trim()
                    var nonTerminal = newGrammar.getNonTerminalForID(head)
                    if (nonTerminal != null) {
                        var splitBody = body.split("|")
                        for (ruleString in splitBody) {
                            var rule = Rule()
                            rule.firstSymbol = nonTerminal
                            nonTerminal.rules.add(rule)

                            var trimmedRS = ruleString.trim()
                            var items = trimmedRS.split(" ")
                            for (item in items) {
                                if (item.contains("T(")) {
                                    var tokenID = item.drop(2).dropLast(1)
                                    var token = Token(TokenType.valueOf(tokenID))
                                    rule.production.add(token)
                                } else {
                                    var newNonterminal = newGrammar.getNonTerminalForID(item)
                                    if (newNonterminal != null){
                                        rule.production.add(newNonterminal)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return newGrammar
        }

        var toy: String = """
start =
    statements
;

statements =
    statement repeatingSemi statements |
    statement repeatingSemi
;

repeatingSemi =
    T(${TokenType.SEMICOLON}) |
    T(${TokenType.SEMICOLON}) repeatingSemi
;

statement =
    declaration |
    print
;

declaration =
    T(${TokenType.VAR}) T(${TokenType.IDENTIFIER}) |
    T(${TokenType.VAR}) T(${TokenType.IDENTIFIER}) T(${TokenType.ASSIGNMENT}) expression
;

assignment =
    T(${TokenType.IDENTIFIER}) T(${TokenType.ASSIGNMENT}) expression
;

print =
    T(${TokenType.PRINT}) expression
;

expression =
    atomicExpression |
    add
;

add =
    mul |
    add T(${TokenType.ADD}) mul
;

mul =
    t |
    mul T(${TokenType.MUL}) t
;

t =
    T(${TokenType.IDENTIFIER}) |
    T(${TokenType.NUMBER})
;

atomicExpression =
    T(${TokenType.NUMBER}) |
    T(${TokenType.STRING}) |
    T(${TokenType.IDENTIFIER})
;

"""
    }
}