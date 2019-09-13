package ilgulee.com.currencycalculator.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ilgulee.com.currencycalculator.R
import ilgulee.com.currencycalculator.databinding.FragmentConversionListBinding


class ConversionListFragment : Fragment() {

    private val conversionListViewModel: ConversionListViewModel by lazy {
        ViewModelProviders.of(this).get(ConversionListViewModel::class.java)
    }

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
        binding.conversionListViewModel = conversionListViewModel
        binding.setLifecycleOwner(this)
        val adapter = ConversionListAdapter()
        binding.conversionList.adapter = adapter
        conversionListViewModel.response.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it.quotes as MutableMap<String, Float>
            }
        })
        return binding.root
    }
}