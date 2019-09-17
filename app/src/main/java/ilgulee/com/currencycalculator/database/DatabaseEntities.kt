package ilgulee.com.currencycalculator.database

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ilgulee.com.currencycalculator.domain.LiveQuote
import java.util.*

const val CURRENT_CURRENCY_ID = 0
@Entity(tableName = "conversion_list")
@TypeConverters(Converter::class)
data class LiveQuoteDatabase(
    val source: String,
    val timestamp: Long,
    val quotes: Map<String, Float> = mutableMapOf()
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_CURRENCY_ID
}

fun LiveQuoteDatabase.asLiveQuoteDomainModel(): LiveQuote {
    Log.d("dbToDomain", this.source)
    return LiveQuote(this.source, this.timestamp, this.quotes)
}

class Converter {
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