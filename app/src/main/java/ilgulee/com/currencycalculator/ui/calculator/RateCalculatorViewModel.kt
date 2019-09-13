package ilgulee.com.currencycalculator.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ilgulee.com.currencycalculator.network.CurrencyLayerApiObject
import ilgulee.com.currencycalculator.network.NetworkResponseConvertCurrencyObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RateCalculatorViewModel : ViewModel() {

    private val key = "a1333d0832b1208792fdd9bd929adcf8"
    private val source = "USD"
    private val destination = "JPY"
    private var amount = 100f

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    init {
        getConvert()
    }

    private fun getConvert() {
        CurrencyLayerApiObject
            .currencyLayerApiService
            .convertCurrency(key, source, destination, amount)
            .enqueue(object : Callback<NetworkResponseConvertCurrencyObject> {
                override fun onFailure(
                    call: Call<NetworkResponseConvertCurrencyObject>,
                    t: Throwable
                ) {
                    _response.value = t.message
                }

                override fun onResponse(
                    call: Call<NetworkResponseConvertCurrencyObject>,
                    response: Response<NetworkResponseConvertCurrencyObject>
                ) {
                    if (response.isSuccessful) {
                        _response.value = response.body().toString()
                    }
                }
            })
    }

}