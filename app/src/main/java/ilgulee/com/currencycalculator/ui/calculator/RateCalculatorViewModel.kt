package ilgulee.com.currencycalculator.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ilgulee.com.currencycalculator.network.CurrencyLayerApiObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RateCalculatorViewModel : ViewModel() {

    private val key = "a1333d0832b1208792fdd9bd929adcf8"
    private val source = "USD"
    private val destination = "JPY"

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    init {
        getConvert()
    }

    private fun getConvert() {
        CurrencyLayerApiObject
            .currencyLayerApiService
            .convertCurrency(key, source, destination, 1.000f).enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    _response.value = t.message
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        _response.value = response.body()
                    }
                }
            })
    }

}