package heraclius

fun main() {
    val regex = Regex("Symbol\\((.*)\\)")
    val matchResult = regex.find("Symbol(dsdsad)")
    if (matchResult != null) {
        println(matchResult.groupValues)
    }
}
