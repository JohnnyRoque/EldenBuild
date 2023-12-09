package com.eldenbuild.ui.itemdetailfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.R
import com.eldenbuild.data.StatsAmount
import com.eldenbuild.databinding.StatsViewDesignBinding

class StatsAmountAdapter(private val context: Context) :
    ListAdapter<StatsAmount, StatsAmountAdapter.StatsAmountViewHolder>(object :
        DiffUtil.ItemCallback<StatsAmount>() {
        override fun areItemsTheSame(oldItem: StatsAmount, newItem: StatsAmount): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: StatsAmount, newItem: StatsAmount): Boolean {
            return oldItem.amount == newItem.amount
        }

    }) {
    class StatsAmountViewHolder(binding: StatsViewDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val statsText : TextView = binding.statsTextName

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsAmountViewHolder {
        return StatsAmountViewHolder(
            StatsViewDesignBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: StatsAmountViewHolder, position: Int) {
        val item =  getItem(position)
        holder.statsText.text = context.resources.getString(R.string.stats_text, item.name,item.amount.toString())

    }
}