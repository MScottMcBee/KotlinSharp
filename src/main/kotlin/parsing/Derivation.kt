package com.mscottmcbee.kotlinsharp.parsing

open class Derivation(
    var left:Derivation?,
    var right:Derivation?,
    var next:Derivation?
){}
