package ilgulee.com.currencycalculator.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ilgulee.com.currencycalculator.network.CurrencyLayerApiObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConversionListViewModel : ViewModel() {
    private val key = "a1333d0832b1208792fdd9bd929adcf8"
    private val source = "JPY"

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    init {
        getLiveList()
    }

    private fun getLiveList() {
        CurrencyLayerApiObject
            .currencyLayerApiService.getLiveList(key, source).enqueue(object : Callback<String> {
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