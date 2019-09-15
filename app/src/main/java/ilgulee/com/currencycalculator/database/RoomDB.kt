package ilgulee.com.currencycalculator.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LiveQuoteDao {
    @Query("select * from conversion_list where source = :source")
    fun getLiveQuote(source: String): LiveData<LiveQuoteDatabase>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLiveQuote(liveQuote: LiveQuoteDatabase)
}

@Database(entities = [LiveQuoteDatabase::class], version = 1, exportSchema = false)
abstract class LiveQuoteRoomDatabase : RoomDatabase() {
    abstract val liveQuoteDao: LiveQuoteDao

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
