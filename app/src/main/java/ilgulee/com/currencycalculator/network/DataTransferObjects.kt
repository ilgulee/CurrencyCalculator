package ilgulee.com.currencycalculator.network

import com.squareup.moshi.Json
import ilgulee.com.currencycalculator.database.CurrencyListDatabase
import ilgulee.com.currencycalculator.database.LiveQuoteDatabase

data class CurrencyListNetworkResponse(
    @Json(name = "currencies")
    val currencies: Map<String, String> = mutableMapOf()
)

data class LiveQuoteNetworkResponse(
    @Json(name = "source")
    val source: String,
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "timestamp")
    val timestamp: Long,
    @Json(name = "quotes")
    val quotes: Map<String, Float> = mutableMapOf()
)

fun LiveQuoteNetworkResponse.asLiveQuoteDatabaseModel(): LiveQuoteDatabase {
    return LiveQuoteDatabase(this.source, this.timestamp, this.quotes)
}

fun CurrencyListNetworkResponse.asCurrencyListDatabaseModel(): CurrencyListDatabase {
    return CurrencyListDatabase(this.currencies)
}

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