package ilgulee.com.currencycalculator.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ilgulee.com.currencycalculator.network.CurrencyLayerApiObject
import ilgulee.com.currencycalculator.network.NetworkResponseLiveListObject
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
            .currencyLayerApiService.getLiveList(key, source)
            .enqueue(object : Callback<NetworkResponseLiveListObject> {
                override fun onFailure(call: Call<NetworkResponseLiveListObject>, t: Throwable) {
                    _response.value = "Failure: ${t.message}"
            }

                override fun onResponse(
                    call: Call<NetworkResponseLiveListObject>,
                    response: Response<NetworkResponseLiveListObject>
                ) {
                if (response.isSuccessful) {
                    _response.value =
                        "Success: retrieved ${response.body()?.quotes?.size.toString()} quotes"
                }
            }
        })
    }
}