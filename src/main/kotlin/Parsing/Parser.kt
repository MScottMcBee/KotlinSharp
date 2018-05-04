package com.mscottmcbee.kotlinsharp

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


class Parser(){


    lateinit var states: Array<List<EarleyState>?>

    fun doIt(production: Production){
        states = arrayOfNulls<List<EarleyState>?>(12)


    }


}