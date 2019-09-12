package ilgulee.com.currencycalculator.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ilgulee.com.currencycalculator.R

class ConversionListFragment : Fragment() {

    private lateinit var conversionListViewModel: ConversionListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        conversionListViewModel =
            ViewModelProviders.of(this).get(ConversionListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_conversion_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        conversionListViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}