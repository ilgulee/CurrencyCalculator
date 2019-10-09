package ilgulee.com.currencycalculator.ui.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxAdapterView
import com.jakewharton.rxbinding2.widget.RxTextView
import ilgulee.com.currencycalculator.R
import ilgulee.com.currencycalculator.databinding.FragmentConversionListBinding
import ilgulee.com.currencycalculator.domain.Currency
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy


class ConversionListFragment : Fragment() {
    private lateinit var viewModel: ConversionListViewModel

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentConversionListBinding>(
            inflater,
            R.layout.fragment_conversion_list,
            container,
            false
        )
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ConversionListViewModel.Factory(application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ConversionListViewModel::class.java)
        binding.conversionListViewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = ConversionListAdapter()
        binding.conversionList.adapter = adapter
        binding.conversionList.layoutManager = LinearLayoutManager(context)
        viewModel.getLiveQuoteList().observe(this, Observer {
            it.let {
                if (it.isNotEmpty()) {
                    adapter.data = it
                }
            }
        })

        val editTextInput: Observable<CharSequence> = RxTextView.textChanges(binding.editTextInput)
        editTextInput
            .filter { it.toString().trim().isNotEmpty() }
            .subscribeBy { viewModel.editText.onNext(it) }


        viewModel.getCurrencyList().observe(this, Observer<List<Currency>> {
            it?.let {
                if (it.isNotEmpty()) {
                    val currencyList: List<Currency> = it
                    val spinnerAdapter = SpinnerDropDownAdapter(context!!, it)
                    binding.spinner.adapter = spinnerAdapter

                    RxAdapterView.itemSelections(binding.spinner)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe { position ->
                            val source = currencyList[position].source
                            viewModel.getLiveQuoteListFromDatabaseAsLiveQuoteList(source)
                            viewModel.getLiveQuoteListFromApiAndSaveToDatabase(source)
                        }
                } else {
                    viewModel.getCurrencyListFromApiAndSaveToDatabase()
                    viewModel.getCurrencyListFromDatabaseAsCurrencyList()
                }
            }
        })

        viewModel.eventNetworkError.observe(this, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        return binding.root
    }


    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}
