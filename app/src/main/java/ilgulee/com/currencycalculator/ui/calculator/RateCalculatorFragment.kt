package ilgulee.com.currencycalculator.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ilgulee.com.currencycalculator.R
import ilgulee.com.currencycalculator.databinding.FragmentRateCalculatorBinding

class RateCalculatorFragment : Fragment() {

    private val rateCalculatorViewModel: RateCalculatorViewModel by lazy {
        ViewModelProviders.of(this).get(RateCalculatorViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil
            .inflate<FragmentRateCalculatorBinding>(
                inflater
                , R.layout.fragment_rate_calculator
                , container, false
            )
        binding.setLifecycleOwner(this)
        binding.rateCalculatorViewModel = rateCalculatorViewModel
        return binding.root
    }
}