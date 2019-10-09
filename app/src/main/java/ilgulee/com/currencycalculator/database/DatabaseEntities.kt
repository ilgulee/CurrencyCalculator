package ilgulee.com.currencycalculator.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ilgulee.com.currencycalculator.domain.Currency
import ilgulee.com.currencycalculator.domain.LiveQuote
import java.util.*

const val CURRENT_CURRENCY_ID = 0

@Entity(tableName = "currency_list")
@TypeConverters(CurrencyListConverter::class)
data class CurrencyListDatabase(
    val currencies: Map<String, String> = mutableMapOf()
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_CURRENCY_ID
}


@Entity(tableName = "conversion_list")
@TypeConverters(LiveQuoteConverter::class)
data class LiveQuoteDatabase(
    @PrimaryKey(autoGenerate = false)
    val source: String,
    val timestamp: Long,
    val quotes: Map<String, Float> = mutableMapOf()
)

fun CurrencyListDatabase.asCurrencyListDomainModel(): List<Currency> {
    val temp = mutableListOf<Currency>()
    this.currencies.map {
        val code = it.key
        val nameAndUnit = it.value
        val currency = Currency(code, nameAndUnit)
        temp.add(currency)
    }
    return temp
}

fun LiveQuoteDatabase.asLiveQuoteDomainModel(): List<LiveQuote> {
    val temp = mutableListOf<LiveQuote>()
    this.quotes.map {
        val sourceFrom = this.source
        val toSource = it.key.substring(3..5)
        val rate = it.value
        val timeStamp = this.timestamp
        val liveQuote = LiveQuote(sourceFrom, toSource, rate, timeStamp)
        temp.add(liveQuote)
    }
    return temp
}

class CurrencyListConverter {
    private val gson = Gson()
    @TypeConverter
    fun stringToMutableMap(data: String?): MutableMap<String, String> {
        if (data == null) {
            return Collections.emptyMap()
        }
        val mutableMapType = object : TypeToken<MutableMap<String, String>>() {}.type
        return gson.fromJson<MutableMap<String, String>>(data, mutableMapType)
    }

    @TypeConverter
    fun mutableMapToString(someObjects: MutableMap<String, String>): String {
        return gson.toJson(someObjects)
    }
}

class LiveQuoteConverter {
    private val gson = Gson()
    @TypeConverter
    fun stringToMutableMap(data: String?): MutableMap<String, Float> {
        if (data == null) {
            return Collections.emptyMap()
        }
        val mutableMapType = object : TypeToken<MutableMap<String, Float>>() {}.type
        return gson.fromJson<MutableMap<String, Float>>(data, mutableMapType)
    }

    @TypeConverter
    fun mutableMapToString(someObjects: MutableMap<String, Float>): String {
        return gson.toJson(someObjects)
    }
}