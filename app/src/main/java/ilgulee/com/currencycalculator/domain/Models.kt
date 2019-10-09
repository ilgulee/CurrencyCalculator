package ilgulee.com.currencycalculator.domain

data class LiveQuote(
    val sourceFrom: String,
    val toSource: String,
    var rate: Float,
    val timeStamp: Long
)

data class Currency(
    val source: String,
    val nameAndUnit: String
)
