package heraclius

import heraclius.core.dict.Dict
import heraclius.core.dict.Symbols

fun main() {
    val dict = Dict()
    val dict1 = Dict()
    val symbol = Symbols.of<Int>("sa")
    val symbol1 = Symbols.of<Int>("sadsa")
    dict[symbol] = 20
    dict[symbol1] = 30
    println(dict + dict1)
}
