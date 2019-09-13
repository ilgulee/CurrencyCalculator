package ilgulee.com.currencycalculator.network

import com.squareup.moshi.Json


data class NetworkResponseLiveListObject(
    @Json(name = "source")
    val source: String,
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "timestamp")
    val timestamp: Long,
    @Json(name = "quotes")
    val quotes: MutableMap<String, Float> = mutableMapOf()
)

data class NetworkResponseConvertCurrencyObject(
    @Json(name = "info")
    val info: Info,
    @Json(name = "query")
    val query: Query,
    @Json(name = "result")
    val result: Double
)

data class Query(
    @Json(name = "amount")
    val amount: Float,
    @Json(name = "from")
    val source: String,
    @Json(name = "to")
    val destination: String
)

data class Info(
    @Json(name = "quote")
    val quote: Float,
    @Json(name = "timestamp")
    val timestamp: Long
)