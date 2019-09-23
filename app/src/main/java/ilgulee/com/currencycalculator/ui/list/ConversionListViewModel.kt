package ilgulee.com.currencycalculator.ui.list

import android.app.Application
import androidx.lifecycle.*
import ilgulee.com.currencycalculator.database.LiveQuoteRoomDatabase
import ilgulee.com.currencycalculator.domain.LiveQuote
import ilgulee.com.currencycalculator.repository.LiveQuoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class ConversionListViewModel(application: Application) : AndroidViewModel(application) {
    val liveQuoteRepository: LiveQuoteRepository by lazy {
        LiveQuoteRepository(LiveQuoteRoomDatabase.getInstance(application))
    }
    private val liveQuoteResponse = liveQuoteRepository.liveQuote
    val currencyListResponse = liveQuoteRepository.currencyList

    val _code = MutableLiveData<String>()
    val code: LiveData<String>
        get() = _code

    val _selectedItemPosition = MutableLiveData<Int>()
    val selectedItemPosition: LiveData<Int>
        get() = _selectedItemPosition

    val newLiveQuoteResponse: LiveData<List<LiveQuote>> = transformWithInput()

    fun transformWithInput(): LiveData<List<LiveQuote>> {
        return Transformations.map(liveQuoteResponse) {
            it?.let { calculatedWithUserInput(it) }
        }
    }

    private fun calculatedWithUserInput(list: List<LiveQuote>): List<LiveQuote> {
        return list.map { it.copy(rate = calculateFloat(it.rate, _input.value!!.toFloat())) }
    }

    private fun calculateFloat(rate: Float, input: Float): Float {
        return rate * input
    }

    private var _input = MutableLiveData<Float>().apply { value = 1f }
    val input: LiveData<Float>
        get() = _input

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
        getCurrencyListFromRepository()
    }

    fun getLiveQuoteFromRepository() {
        coroutineScope.launch {
            try {
                liveQuoteRepository.refreshLiveQuote()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                _eventNetworkError.value = true
            }
        }
    }

    private fun getCurrencyListFromRepository() {
        coroutineScope.launch {
            try {
                liveQuoteRepository.refreshCurrencyList()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                _eventNetworkError.value = true
            }
        }
    }

    fun getInputFromEditText(userInput: String) {
        if (userInput.isNotEmpty()) {
            _input.value = userInput.toFloat()
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConversionListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ConversionListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
