package ilgulee.com.currencycalculator.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://apilayer.net/api/"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL).build()

interface CurrencyLayerApiService {

    @GET("convert")
    fun convertCurrency(
        @Query("access_key") key: String,
        @Query("from") source: String,
        @Query("to") destination: String,
        @Query("amount") amount: Float,
        @Query("format") format: Int = 1
    ): Deferred<NetworkResponseConvertCurrencyObject>

    @GET("live")
    fun getLiveList(
        @Query("access_key") key: String,
        @Query("source") source: String,
        @Query("format") format: Int = 1
    ): Deferred<NetworkResponseLiveListObject>
}

object CurrencyLayerApiObject {
    val currencyLayerApiService: CurrencyLayerApiService by lazy {
        retrofit.create(CurrencyLayerApiService::class.java)
    }
}