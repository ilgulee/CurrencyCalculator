package ilgulee.com.currencycalculator.ui.list

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import ilgulee.com.currencycalculator.database.LiveQuoteRoomDatabase
import ilgulee.com.currencycalculator.database.asCurrencyListDomainModel
import ilgulee.com.currencycalculator.database.asLiveQuoteDomainModel
import ilgulee.com.currencycalculator.domain.Currency
import ilgulee.com.currencycalculator.domain.LiveQuote
import ilgulee.com.currencycalculator.network.asCurrencyListDatabaseModel
import ilgulee.com.currencycalculator.network.asLiveQuoteDatabaseModel
import ilgulee.com.currencycalculator.repository.LiveQuoteRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ConversionListViewModel(application: Application) : AndroidViewModel(application) {

    private val liveQuoteRepository: LiveQuoteRepository by lazy {
        LiveQuoteRepository(LiveQuoteRoomDatabase.getInstance(application))
    }

    private val disposable = CompositeDisposable()

    val editText: BehaviorSubject<CharSequence> =
        BehaviorSubject.create<CharSequence>()

    private val spinnerItems = MutableLiveData<List<Currency>>()
    private val currencyListSubject: BehaviorSubject<MutableList<Currency>> =
        BehaviorSubject.createDefault(mutableListOf())

    private val recyclerViewItems = MutableLiveData<List<LiveQuote>>()
    private val liveQuoteListSubject: BehaviorSubject<MutableList<LiveQuote>> =
        BehaviorSubject.createDefault(mutableListOf())

    private var _eventNetworkError = MutableLiveData<Boolean>()
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>()
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    init {
        _eventNetworkError.value = false
        _isNetworkErrorShown.value = false

        currencyListSubject
            .subscribe { currencies ->
                spinnerItems.value = currencies
            }.addTo(disposable)

        liveQuoteListSubject.subscribe { liveQuotes ->
            recyclerViewItems.value = liveQuotes
        }.addTo(disposable)

        editText.subscribe {
            makeNewLiveQuoteListWithCalculation(Observable.just(it))
        }.addTo(disposable)
    }

    fun getCurrencyList(): LiveData<List<Currency>> {
        return spinnerItems
    }

    fun getLiveQuoteList(): LiveData<List<LiveQuote>> {
        return recyclerViewItems
    }

    @SuppressLint("CheckResult")
    fun makeNewLiveQuoteListWithCalculation(input: Observable<CharSequence>) {
        input.subscribeOn(Schedulers.computation())
            .map {
                val inputFloat = it.toString().toFloat()
                val calculatedList = mutableListOf<LiveQuote>()
                calculatedList.addAll(liveQuoteListSubject.value!!)
                val temp = mutableListOf<LiveQuote>()
                for (quote in calculatedList) {

                    val newRate = quote.rate * inputFloat
                    temp.add(LiveQuote(quote.sourceFrom, quote.toSource, newRate, quote.timeStamp))
                }
                temp
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { recyclerViewItems.value = it }.addTo(disposable)
    }

    fun getLiveQuoteListFromDatabaseAsLiveQuoteList(source: String) {
        liveQuoteRepository.getLiveQuoteListFromDatabaseAsLiveQuoteList(source)
            .map { it.asLiveQuoteDomainModel() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { liveQuoteListSubject.onNext(it as MutableList<LiveQuote>) }
            .addTo(disposable)
    }

    fun getCurrencyListFromDatabaseAsCurrencyList() {
        liveQuoteRepository.currencyListDatabase
            .map { data ->
                data.asCurrencyListDomainModel()
            }
            .filter { data -> data.isNotEmpty() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { currencyListSubject.onNext(it as MutableList<Currency>) }
            ).addTo(disposable)
    }

    fun getCurrencyListFromApiAndSaveToDatabase() {
        liveQuoteRepository.currencyListFromApi
            .map { response -> response.asCurrencyListDatabaseModel() }
            .subscribeBy(
                onNext = {
                    liveQuoteRepository.saveCurrencyDataToDatabase(it)
                },
                onError = {
                    _eventNetworkError.value = true
                }
            ).addTo(disposable)
    }

    @SuppressLint("CheckResult")
    fun getLiveQuoteListFromApiAndSaveToDatabase(source: String) {
        liveQuoteRepository.getLiveQuoteListFromApi(source)
            .subscribeOn(Schedulers.io())
            .map { it.asLiveQuoteDatabaseModel() }
            .subscribeBy(
                onSuccess = { liveQuoteRepository.saveLiveQuoteDataToDatabase(it) },
                onError = { _eventNetworkError.value = true }
            ).addTo(disposable)
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConversionListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ConversionListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}
