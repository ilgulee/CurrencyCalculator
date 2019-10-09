package ilgulee.com.currencycalculator.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ilgulee.com.currencycalculator.R
import ilgulee.com.currencycalculator.domain.Currency

class SpinnerDropDownAdapter(context: Context, var currencyList: List<Currency>) :
    ArrayAdapter<Currency>(context, 0, currencyList) {

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.text_item_view,
            parent,
            false
        )
        val currency = getItem(position)
        (view as TextView).text = currency?.nameAndUnit
        return view
    }
}