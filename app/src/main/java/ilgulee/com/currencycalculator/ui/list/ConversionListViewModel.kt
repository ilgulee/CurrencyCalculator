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
    val response: LiveData<LiveQuote> = liveQuoteRepository.liveQuote
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
        getLiveListFromRepository()
    }

    fun getLiveListFromRepository() {
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