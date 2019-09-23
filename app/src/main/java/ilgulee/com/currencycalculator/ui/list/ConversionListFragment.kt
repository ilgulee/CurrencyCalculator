package ilgulee.com.currencycalculator.ui.list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ilgulee.com.currencycalculator.R
import ilgulee.com.currencycalculator.databinding.FragmentConversionListBinding
import ilgulee.com.currencycalculator.domain.Currency


class ConversionListFragment : Fragment() {
    private lateinit var viewModel: ConversionListViewModel

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
        binding.setLifecycleOwner(this)
        val adapter = ConversionListAdapter()
        binding.conversionList.adapter = adapter
        viewModel.newLiveQuoteResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })
        viewModel.eventNetworkError.observe(this, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
        viewModel.currencyListResponse.observe(this, Observer {
            it?.let {
                val currencyList = it
                val nameAndUnitList = it.map { it.nameAndUnit }.toMutableList()
                nameAndUnitList.add(0, "SELECT")
                val nameAndUnitArray = nameAndUnitList.toTypedArray()
                spinner(binding, nameAndUnitArray, currencyList)
            }
        })

        binding.editTextInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    Log.d("charsequence", s.toString())
                    viewModel.getInputFromEditText(s.toString())
                }
            }
        })

        viewModel.input.observe(this, Observer { it ->
            it?.let {
                Log.d("input", it.toString())
                viewModel.transformWithInput()
            }
        })

        viewModel.code.observe(this, Observer {
            it?.let {
                viewModel.getLiveQuoteFromRepository()
            }
        })
        return binding.root
    }

    private fun spinner(
        binding: FragmentConversionListBinding,
        nameArray: Array<String>,
        currencyList: List<Currency>
    ) {
        val spinnerAdapter = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_item,
            nameArray
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val nameAndUnit = parent?.selectedItem.toString()
                if (nameAndUnit != "SELECT") {
                    viewModel._selectedItemPosition.value = parent?.selectedItemPosition
                    val code =
                        currencyList.filter { it.nameAndUnit == nameAndUnit }.map { it.code }.last()
                    viewModel._code.value = code
                    viewModel.liveQuoteRepository.code = code
                }
            }
        }
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}