package ilgulee.com.currencycalculator.network

import ilgulee.com.currencycalculator.KEY
import io.reactivex.Observable
import io.reactivex.Single

object CurrencyLayerApi {
    private val service = CurrencyLayerApiService.create()

    fun getCurrencyListFromApi(): Observable<CurrencyListNetworkResponse> {
        return service.getCurrencyListFromApi(KEY)
    }

    fun getQuoteListFromApi(source: String): Single<LiveQuoteNetworkResponse> {
        return service.getLive(KEY, source)
    }
}