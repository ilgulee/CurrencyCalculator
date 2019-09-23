package ilgulee.com.currencycalculator.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ilgulee.com.currencycalculator.KEY
import ilgulee.com.currencycalculator.database.LiveQuoteRoomDatabase
import ilgulee.com.currencycalculator.database.asCurrencyListDomainModel
import ilgulee.com.currencycalculator.database.asLiveQuoteDomainModel
import ilgulee.com.currencycalculator.domain.Currency
import ilgulee.com.currencycalculator.domain.LiveQuote
import ilgulee.com.currencycalculator.network.CurrencyLayerApiObject
import ilgulee.com.currencycalculator.network.asCurrencyListDatabaseModel
import ilgulee.com.currencycalculator.network.asLiveQuoteDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LiveQuoteRepository(private val database: LiveQuoteRoomDatabase) {

    lateinit var code: String

    var liveQuote: LiveData<List<LiveQuote>> = Transformations
        .map(database.liveQuoteDao.getLiveQuote()) {
            it?.asLiveQuoteDomainModel()
        }

    var currencyList: LiveData<List<Currency>> =
        Transformations.map(database.currencyListDao.getCurrencyList()) {
            it?.asCurrencyListDomainModel()
        }

    suspend fun refreshLiveQuote() {
        withContext((Dispatchers.IO)) {
            val liveQuote =
                CurrencyLayerApiObject.currencyLayerApiService.getLiveAsync(KEY, code).await()
            database.liveQuoteDao.insertLiveQuote(liveQuote.asLiveQuoteDatabaseModel())
        }
    }

    suspend fun refreshCurrencyList() {
        withContext(Dispatchers.IO) {
            val currencyList =
                CurrencyLayerApiObject.currencyLayerApiService.getCurrencyListAsync(KEY).await()
            database.currencyListDao.insertCurrencyList(currencyList.asCurrencyListDatabaseModel())
        }
    }
}
