package ilgulee.com.currencycalculator.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ilgulee.com.currencycalculator.network.CurrencyLayerApiObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class RateCalculatorViewModel : ViewModel() {

    private val key = "a1333d0832b1208792fdd9bd929adcf8"
    private val source = "USD"
    private val destination = "JPY"
    private var amount = 100f

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getConvert()
    }

    private fun getConvert() {
        coroutineScope.launch {
            val deferredNetworkResponseConvertCurrencyObject = CurrencyLayerApiObject
                .currencyLayerApiService.convertCurrency(key, source, destination, amount)
            try {
                val convertCurrency = deferredNetworkResponseConvertCurrencyObject.await()
                _response.value =
                    "Success: retrieved ${convertCurrency}"
            } catch (networkError: IOException) {
                _response.value = "Failure: ${networkError.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}