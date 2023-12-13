package com.eldenbuild.ui.builds_overview_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.data.BuildCategories
import com.eldenbuild.databinding.BuildSelectionVerticalBinding

const val TAG = "ItemId"

class OverviewRecyclerAdapter(val buildDetail: (String) -> Unit) :
    ListAdapter<BuildCategories, OverviewRecyclerAdapter.BuildViewHolder>(object :
        DiffUtil.ItemCallback<BuildCategories>() {

        override fun areItemsTheSame(oldItem: BuildCategories, newItem: BuildCategories): Boolean {
            return oldItem.buildId == newItem.buildId
        }

        override fun areContentsTheSame(
            oldItem: BuildCategories,
            newItem: BuildCategories
        ): Boolean {
            return oldItem.buildItems.containsAll(newItem.buildItems)
        }

    }) {
    class BuildViewHolder(val binding: BuildSelectionVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(buildCategories: BuildCategories) {
            binding.apply {
                overViewBuild = buildCategories
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildViewHolder {
        return BuildViewHolder(
            BuildSelectionVerticalBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BuildViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            buildDetail(item.buildId.toString())
        }
    }
}
