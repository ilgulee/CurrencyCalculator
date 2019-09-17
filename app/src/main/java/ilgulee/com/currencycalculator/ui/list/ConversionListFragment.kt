package ilgulee.com.currencycalculator.ui.list

import android.os.Bundle
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
        viewModel.response.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it.quotes as MutableMap<String, Float>
            }
        })
        viewModel.eventNetworkError.observe(this, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        val spinnerAdapter = ArrayAdapter.createFromResource(
            context!!,
            R.array.country_array,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val item = parent?.selectedItem.toString()
                Log.d("Nothing selected: ", item)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = parent?.getItemAtPosition(position).toString()
                Log.d("Spinner selected: ", item)
                viewModel.liveQuoteRepository.source = item
                viewModel.getLiveListFromRepository()
            }
        }
        return binding.root
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}