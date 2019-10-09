package ilgulee.com.currencycalculator.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ilgulee.com.currencycalculator.BASE_URL
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyLayerApiService {

    companion object {

        fun create(): CurrencyLayerApiService {
            val logger = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttp = OkHttpClient
                .Builder()
                .addInterceptor(logger)

            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttp.build())
                .build()
            return retrofit.create(CurrencyLayerApiService::class.java)
        }
    }

    @GET("live")
    fun getLive(
        @Query("access_key") key: String,
        @Query("source") source: String,
        @Query("format") format: Int = 1
    ): Single<LiveQuoteNetworkResponse>

    @GET("list")
    fun getCurrencyListFromApi(
        @Query("access_key") key: String,
        @Query("format") format: Int = 1
    ): Observable<CurrencyListNetworkResponse>

}