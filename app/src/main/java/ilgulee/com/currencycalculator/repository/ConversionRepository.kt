package ilgulee.com.currencycalculator.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ilgulee.com.currencycalculator.KEY
import ilgulee.com.currencycalculator.database.LiveQuoteRoomDatabase
import ilgulee.com.currencycalculator.database.asLiveQuoteDomainModel
import ilgulee.com.currencycalculator.domain.LiveQuote
import ilgulee.com.currencycalculator.network.CurrencyLayerApiObject
import ilgulee.com.currencycalculator.network.asLiveQuoteDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class LiveQuoteRepository(private val database: LiveQuoteRoomDatabase, private val source: String) {

    val liveQuote: LiveData<LiveQuote> = Transformations
        .map(database.liveQuoteDao.getLiveQuote(source)) {
            it.asLiveQuoteDomainModel()
        }

    suspend fun refreshLiveQuote() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh liveQuote is called");
            val liveQuote =
                CurrencyLayerApiObject.currencyLayerApiService.getLiveList(KEY, source).await()
            database.liveQuoteDao.insertLiveQuote(liveQuote.asLiveQuoteDatabaseModel())
        }
    }

}