package ilgulee.com.currencycalculator.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://apilayer.net/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL).build()

interface CurrencyLayerApiService {

    @GET("convert")
    fun convertCurrency(
        @Query("access_key") key: String,
        @Query("from") source: String,
        @Query("to") destination: String,
        @Query("amount") amount: Float,
        @Query("format") format: Int = 1
    ): Call<String>

    @GET("live")
    fun getLiveList(
        @Query("access_key") key: String,
        @Query("source") source: String,
        @Query("format") format: Int = 1
    ): Call<String>
}

object CurrencyLayerApiObject {
    val currencyLayerApiService: CurrencyLayerApiService by lazy {
        retrofit.create(CurrencyLayerApiService::class.java)
    }
}