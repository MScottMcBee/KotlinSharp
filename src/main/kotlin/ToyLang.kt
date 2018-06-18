package com.mscottmcbee.kotlinsharp

import com.mscottmcbee.kotlinsharp.parsing.Grammar
import com.mscottmcbee.kotlinsharp.parsing.Nonterminal
import com.mscottmcbee.kotlinsharp.parsing.Rule


/*

start
: statements
;

statements
(used by block, functionLiteral)
: SEMI* statement{SEMI+} SEMI*
;

statement
(used by statements)
: declaration
: print
: read
: for
;

declaration
: VAR assignment
;

assignment
:IDENTIFIER ASSIGNMENT expr
;

expr
: atomicExpression
;

atomicExpresson
: NUMBER
: STRING
: IDENTIFIER
;

print
: PRINT expr
;

read
: READ IDENTIFIER
;

for
: FOR assignment TO expr DO statements END


 */


class ToyLang {

    fun getGrammar(): Grammar {

        var newGrammar = Grammar()

     /*   var addTerminal = Nonterminal("addT").also{
            Rule().also { prod ->
                prod.firstSymbol = Token(TokenType.ADD)
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var mulTerminal = Nonterminal("mulT").also{
            Rule().also { prod ->
                prod.firstSymbol = Token(TokenType.MUL)
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var semiTerminal = Nonterminal("semiT").also{
            Rule().also { prod ->
                prod.firstSymbol = Token(TokenType.SEMICOLON)
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var numTerminal = Nonterminal("numT").also{
            Rule().also { prod ->
                prod.firstSymbol = Token(TokenType.NUMBER)
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }*/

        var readNonterminal = Nonterminal("READ").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(Token(TokenType.IDENTIFIER))
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var atomicExpressionNonterminal = Nonterminal("ATOMICEXPRESSION").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(Token(TokenType.NUMBER))
                it.rules.add(prod)
            }
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(Token(TokenType.STRING))
                it.rules.add(prod)
            }
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(Token(TokenType.IDENTIFIER))
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var tNonterminal = Nonterminal("T").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(Token(TokenType.NUMBER))
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var mulNonterminal = Nonterminal("MUL").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(tNonterminal)
                it.rules.add(prod)
            }

            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(it)
                prod.production.add(Token(TokenType.MUL))
                prod.production.add(tNonterminal)
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var addNonterminal = Nonterminal("ADD").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(mulNonterminal)
                it.rules.add(prod)
            }

            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(it)
                prod.production.add(Token(TokenType.ADD))
                prod.production.add(mulNonterminal)
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var parenNonterminal = Nonterminal("PAREN").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(Token(TokenType.OPENPAREN))
                prod.production.add(addNonterminal)
                prod.production.add(Token(TokenType.CLOSEPAREN))

                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var expressionNonterminal = Nonterminal("EXPRESSION").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(atomicExpressionNonterminal)
                it.rules.add(prod)
            }
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(addNonterminal)
                it.rules.add(prod)
            }
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(parenNonterminal)
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var printNonterminal = Nonterminal("PRINT").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(Token(TokenType.PRINT))
                prod.production.add(expressionNonterminal)
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var assignmentNonterminal = Nonterminal("ASSIGNMENT").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(Token(TokenType.IDENTIFIER))
                prod.production.add(Token(TokenType.ASSIGNMENT))
                prod.production.add(expressionNonterminal)
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var declarationNonterminal = Nonterminal("DECLARATION").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(Token(TokenType.VAR))
                prod.production.add(assignmentNonterminal)
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }


        var statementsNonterminal = Nonterminal("STATEMENTS")

        //FOR assignment TO expr DO statements END
        var forNonterminal = Nonterminal("FOR").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(Token(TokenType.FOR))
                prod.production.add(assignmentNonterminal)
                prod.production.add(Token(TokenType.TO))
                prod.production.add(expressionNonterminal)
                prod.production.add(Token(TokenType.DO))
                prod.production.add(statementsNonterminal)
                prod.production.add(Token(TokenType.END))
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var statementNonterminal = Nonterminal("STATEMENT").also {
           /* Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(declarationNonterminal)
                it.rules.add(prod)
            }
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(readNonterminal)
                it.rules.add(prod)
            }
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(printNonterminal)
                it.rules.add(prod)
            }
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(forNonterminal)
                it.rules.add(prod)
            }*/
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(addNonterminal)
                it.rules.add(prod)
            }


            newGrammar.productions.add(it)
        }


        var repeatingSemiNonterminal = Nonterminal("MAYBESEMI").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(Token(TokenType.SEMICOLON))
                it.rules.add(prod)
            }
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(Token(TokenType.SEMICOLON))
                prod.production.add(it)
                it.rules.add(prod)
            }


            newGrammar.productions.add(it)
        }


        statementsNonterminal.also {
            //:statement maybeSemi statements
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(statementNonterminal)
                prod.production.add(repeatingSemiNonterminal)
                prod.production.add(it)
                it.rules.add(prod)
            }
            //:statement maybeSemi
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(statementNonterminal)
                prod.production.add(repeatingSemiNonterminal)
                it.rules.add(prod)
            }
            //:maybeSemi statement maybeSemi statements
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(repeatingSemiNonterminal)
                prod.production.add(statementNonterminal)
                prod.production.add(repeatingSemiNonterminal)
                prod.production.add(it)
                it.rules.add(prod)
            }
            //:maybeSemi statement maybeSemi
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(repeatingSemiNonterminal)
                prod.production.add(statementNonterminal)
                prod.production.add(repeatingSemiNonterminal)
                it.rules.add(prod)
            }
            //:maybeSemi statement maybeSemi statements maybeSemi
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(repeatingSemiNonterminal)
                prod.production.add(statementNonterminal)
                prod.production.add(repeatingSemiNonterminal)
                prod.production.add(it)
                prod.production.add(repeatingSemiNonterminal)
                it.rules.add(prod)
            }
            //:maybeSemi statement maybeSemi
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(repeatingSemiNonterminal)
                prod.production.add(statementNonterminal)
                prod.production.add(repeatingSemiNonterminal)
                it.rules.add(prod)
            }


            newGrammar.productions.add(it)
        }
/*
        statements
        (used by block, functionLiteral)
        : SEMI* statement{SEMI+} SEMI*
        ;

        statements'

        maybeSemi
        :SEMI
        :SEMI maybeSemi

  */

        var startNonterminal = Nonterminal("START").also {
            Rule().also { prod ->
                prod.firstSymbol = it
                prod.production.add(addNonterminal)
                it.rules.add(prod)
            }

            newGrammar.productions.add(it)
        }

        return newGrammar

    }

}