package semantics

class IdentifierData(val name:String){
    var type:String = ""

    override fun toString(): String {
        return "$name: $type"
    }

}