package ilgulee.com.currencycalculator.repository

import ilgulee.com.currencycalculator.database.CurrencyListDatabase
import ilgulee.com.currencycalculator.database.LiveQuoteDatabase
import ilgulee.com.currencycalculator.database.LiveQuoteRoomDatabase
import ilgulee.com.currencycalculator.network.CurrencyLayerApi
import ilgulee.com.currencycalculator.network.CurrencyListNetworkResponse
import ilgulee.com.currencycalculator.network.LiveQuoteNetworkResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


class LiveQuoteRepository(private val database: LiveQuoteRoomDatabase) {

    fun getLiveQuoteListFromDatabaseAsLiveQuoteList(source: String): Observable<LiveQuoteDatabase> {
        return database.liveQuoteDao.getLiveQuote(source).subscribeOn(Schedulers.io())
    }

    val currencyListDatabase: Observable<CurrencyListDatabase> =
        database.currencyListDao
            .getCurrencyListDatabase()
            .subscribeOn(Schedulers.io())

    val currencyListFromApi: Observable<CurrencyListNetworkResponse> =
        CurrencyLayerApi
            .getCurrencyListFromApi()
            .subscribeOn(Schedulers.io())

    fun saveCurrencyDataToDatabase(currencyListDatabase: CurrencyListDatabase) {
        database.currencyListDao.insertCurrencyList(currencyListDatabase)
    }

    fun getLiveQuoteListFromApi(source: String): Single<LiveQuoteNetworkResponse> {
        return CurrencyLayerApi.getQuoteListFromApi(source)
    }

    fun saveLiveQuoteDataToDatabase(liveQuoteDatabase: LiveQuoteDatabase) {
        database.liveQuoteDao.insertLiveQuote(liveQuoteDatabase)
    }

}
