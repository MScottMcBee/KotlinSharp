package com.mscottmcbee.kotlinsharp

import com.mscottmcbee.kotlinsharp.parsing.Grammar
import com.mscottmcbee.kotlinsharp.parsing.Nonterminal
import com.mscottmcbee.kotlinsharp.parsing.Production
import java.beans.Expression


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

        var readNonterminal = Nonterminal("READ").also {
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(Token(TokenType.IDENTIFIER))
                it.productions.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var atomicExpressionNonterminal = Nonterminal("ATOMICEXPRESSION").also {
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(Token(TokenType.NUMBER))
                it.productions.add(prod)
            }
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(Token(TokenType.STRING))
                it.productions.add(prod)
            }
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(Token(TokenType.IDENTIFIER))
                it.productions.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var expressionNonterminal = Nonterminal("EXPRESSION").also {
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(atomicExpressionNonterminal)
                it.productions.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var printNonterminal = Nonterminal("PRINT").also {
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(Token(TokenType.PRINT))
                prod.symbols.add(expressionNonterminal)
                it.productions.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var assignmentNonterminal = Nonterminal("ASSIGNMENT").also {
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(Token(TokenType.IDENTIFIER))
                prod.symbols.add(Token(TokenType.ASSIGNMENT))
                prod.symbols.add(expressionNonterminal)
                it.productions.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var declarationNonterminal = Nonterminal("DECLARATION").also {
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(Token(TokenType.VAR))
                prod.symbols.add(assignmentNonterminal)
                it.productions.add(prod)
            }

            newGrammar.productions.add(it)
        }


        var statementsNonterminal = Nonterminal("STATEMENTS")

        //FOR assignment TO expr DO statements END
        var forNonterminal = Nonterminal("FOR").also {
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(Token(TokenType.FOR))
                prod.symbols.add(assignmentNonterminal)
                prod.symbols.add(Token(TokenType.TO))
                prod.symbols.add(expressionNonterminal)
                prod.symbols.add(Token(TokenType.DO))
                prod.symbols.add(statementsNonterminal)
                prod.symbols.add(Token(TokenType.END))
                it.productions.add(prod)
            }

            newGrammar.productions.add(it)
        }

        var statementNonterminal = Nonterminal("STATEMENT").also {
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(declarationNonterminal)
                it.productions.add(prod)
            }
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(readNonterminal)
                it.productions.add(prod)
            }
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(printNonterminal)
                it.productions.add(prod)
            }
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(forNonterminal)
                it.productions.add(prod)
            }


            newGrammar.productions.add(it)
        }


        var repeatingSemiNonterminal = Nonterminal("maybeSemi").also {
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(Token(TokenType.SEMICOLON))
                it.productions.add(prod)
            }
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(Token(TokenType.SEMICOLON))
                prod.symbols.add(it)
                it.productions.add(prod)
            }


            newGrammar.productions.add(it)
        }


        statementsNonterminal.also {
            //:statement maybeSemi statements
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(statementNonterminal)
                prod.symbols.add(repeatingSemiNonterminal)
                prod.symbols.add(it)
                it.productions.add(prod)
            }
            //:statement maybeSemi
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(statementNonterminal)
                prod.symbols.add(repeatingSemiNonterminal)
                it.productions.add(prod)
            }
            //:maybeSemi statement maybeSemi statements
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(repeatingSemiNonterminal)
                prod.symbols.add(statementNonterminal)
                prod.symbols.add(repeatingSemiNonterminal)
                prod.symbols.add(it)
                it.productions.add(prod)
            }
            //:maybeSemi statement maybeSemi
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(repeatingSemiNonterminal)
                prod.symbols.add(statementNonterminal)
                prod.symbols.add(repeatingSemiNonterminal)
                it.productions.add(prod)
            }
            //:maybeSemi statement maybeSemi statements maybeSemi
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(repeatingSemiNonterminal)
                prod.symbols.add(statementNonterminal)
                prod.symbols.add(repeatingSemiNonterminal)
                prod.symbols.add(it)
                prod.symbols.add(repeatingSemiNonterminal)
                it.productions.add(prod)
            }
            //:maybeSemi statement maybeSemi
            Production().also { prod ->
                prod.symbols.add(it)
                prod.symbols.add(repeatingSemiNonterminal)
                prod.symbols.add(statementNonterminal)
                prod.symbols.add(repeatingSemiNonterminal)
                it.productions.add(prod)
            }
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




        statementsNonterminal.also {

        }

        return newGrammar

    }

}