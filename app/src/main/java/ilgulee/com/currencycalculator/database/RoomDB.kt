package ilgulee.com.currencycalculator.database

import android.content.Context
import androidx.room.*
import io.reactivex.Observable

@Dao
interface LiveQuoteDao {
    @Query("select * from conversion_list where source = :source")
    fun getLiveQuote(source: String): Observable<LiveQuoteDatabase>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLiveQuote(liveQuote: LiveQuoteDatabase)
}

@Dao
interface CurrencyListDao {
    @Query("select * from currency_list where id = $CURRENT_CURRENCY_ID")
    fun getCurrencyListDatabase(): Observable<CurrencyListDatabase>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencyList(currencyList: CurrencyListDatabase)
}

@Database(
    entities = [LiveQuoteDatabase::class, CurrencyListDatabase::class],
    version = 1,
    exportSchema = false
)
abstract class LiveQuoteRoomDatabase : RoomDatabase() {
    abstract val liveQuoteDao: LiveQuoteDao
    abstract val currencyListDao: CurrencyListDao

    companion object {
        @Volatile
        private var INSTANCE: LiveQuoteRoomDatabase? = null

        fun getInstance(context: Context): LiveQuoteRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LiveQuoteRoomDatabase::class.java,
                        "live_quote_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
