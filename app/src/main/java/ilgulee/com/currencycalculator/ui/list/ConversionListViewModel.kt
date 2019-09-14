package ilgulee.com.currencycalculator.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ilgulee.com.currencycalculator.domain.LiveQuote
import ilgulee.com.currencycalculator.network.CurrencyLayerApiObject
import ilgulee.com.currencycalculator.network.asLiveQuoteDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class ConversionListViewModel : ViewModel() {
    private val key = "a1333d0832b1208792fdd9bd929adcf8"
    private val source = "JPY"

    private val _response = MutableLiveData<LiveQuote>()
    val response: LiveData<LiveQuote> = _response
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _eventNetworkError = MutableLiveData<Boolean>()
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>()
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    init {
        _eventNetworkError.value = false
        _isNetworkErrorShown.value = false
        getLiveList()
    }

    private fun getLiveList() {
        coroutineScope.launch {
            val deferredCurrencyLayerApiService = CurrencyLayerApiObject
                .currencyLayerApiService.getLiveList(key, source)
            try {
                val liveList = deferredCurrencyLayerApiService.await()
                _response.postValue(liveList.asLiveQuoteDomainModel())
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                _eventNetworkError.value = true
            }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}