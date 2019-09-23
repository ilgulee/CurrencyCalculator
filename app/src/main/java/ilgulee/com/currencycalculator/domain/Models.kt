package ilgulee.com.currencycalculator.domain

data class LiveQuote(
    val codeFrom: String,
    val code: String,
    val rate: Float
)

data class Currency(
    val code: String,
    val nameAndUnit: String
)
