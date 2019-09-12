package ilgulee.com.currencycalculator.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ilgulee.com.currencycalculator.R

class RateCalculatorFragment : Fragment() {

    private lateinit var rateCalculatorViewModel: RateCalculatorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rateCalculatorViewModel =
            ViewModelProviders.of(this).get(RateCalculatorViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_rate_calculator, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        rateCalculatorViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}