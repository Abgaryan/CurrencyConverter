package com.matso.converter.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.matso.converter.Constants.DECIMAL_FORMAT
import com.matso.converter.R
import com.matso.converter.comman.toFlagEmoji
import com.matso.converter.comman.underline
import kotlinx.android.synthetic.main.rate_view.view.*
import kotlinx.android.synthetic.main.rate_view_item.view.*
import java.util.*

class RecyclerViewAdapter(private val rateItemClickListener: RateItemClickListener) :
    RecyclerView.Adapter<RecyclerViewAdapter.RateViewHolder>() {

    private val rateList: MutableList<Pair<String, Double>> = mutableListOf()


    fun setRates(newRateList: List<Pair<String, Double>>) {
        if (this.rateList.isEmpty()) {
            this.rateList.addAll(newRateList)
            notifyItemRangeInserted(0, rateList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = this@RecyclerViewAdapter.rateList.size

                override fun getNewListSize() = newRateList.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    this@RecyclerViewAdapter.rateList[oldItemPosition] == newRateList[newItemPosition]


                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    newRateList[newItemPosition] == rateList[oldItemPosition]

            })
            this.rateList.clear()
            this.rateList.addAll(newRateList)
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rate_view_item, parent, false)
        return RateViewHolder(view, rateItemClickListener)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) =
        holder.bind(rateList[position])

    override fun getItemCount() = rateList.size


    class RateViewHolder(personView: View, itemClickListener: RateItemClickListener) :
        RecyclerView.ViewHolder(personView) {

        private val content: ViewGroup = itemView.findViewById(R.id.rate_item_content)
        private var rate: Pair<String, Double>? = null

        init {
            content.setOnClickListener { _ ->
                rate?.let {
                    itemClickListener.onRateItemClick(it)
                }
            }
        }

        fun bind(ratePair: Pair<String, Double>) = with(itemView) {
            rate = ratePair
            val currency: Currency = Currency.getInstance(ratePair.first)
            itemView.tvSymbol.text = ratePair.first.dropLast(1).toFlagEmoji()
            itemView.tvName.text = ratePair.first
            itemView.tvDisplayName.text = currency.displayName
            itemView.tvRate.text = DECIMAL_FORMAT.format(ratePair.second)
            itemView.tvRate.underline()

        }
    }


}





