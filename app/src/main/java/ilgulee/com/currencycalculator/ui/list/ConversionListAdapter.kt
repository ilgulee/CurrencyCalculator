package ilgulee.com.currencycalculator.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ilgulee.com.currencycalculator.R
import ilgulee.com.currencycalculator.TextItemViewHolder

class ConversionListAdapter : RecyclerView.Adapter<TextItemViewHolder>() {
    var data = mutableMapOf<String, Float>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val items = data.toList()
        val item = items[position]
        holder.textView.text = item.toString()
    }
}