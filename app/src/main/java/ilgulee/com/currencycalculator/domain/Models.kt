package ilgulee.com.currencycalculator.domain

data class LiveQuote(
    val source: String,
    val timestamp: Long,
    val quotes: Map<String, Float> = mutableMapOf()
)