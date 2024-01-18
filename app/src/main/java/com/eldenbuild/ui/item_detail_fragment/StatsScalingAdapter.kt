package com.eldenbuild.ui.item_detail_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.data.database.StatsScaling
import com.eldenbuild.databinding.StatsViewDesignBinding
import androidx.recyclerview.widget.ListAdapter
import com.eldenbuild.R


class StatsScalingAdapter(private val context:Context) :
    ListAdapter<StatsScaling, StatsScalingAdapter.StatsScalingViewHolder>(StatsCallback) {
    class StatsScalingViewHolder(binding: StatsViewDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val statsText : TextView = binding.statsTextName

    }

    companion object StatsCallback : DiffUtil.ItemCallback<StatsScaling>() {
        override fun areItemsTheSame(oldItem: StatsScaling, newItem: StatsScaling): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: StatsScaling, newItem: StatsScaling): Boolean {
            return oldItem.scaling == newItem.scaling
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsScalingViewHolder {
        return StatsScalingViewHolder(
            StatsViewDesignBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: StatsScalingViewHolder, position: Int) {
        val item = getItem(position)
        holder.statsText.text = context.resources.getString(R.string.stats_text, item.name, item.scaling)
    }


}