package com.eldenbuild.ui.builds_overview_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.R
import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.databinding.BuildSelectionVerticalBinding

const val TAG = "ItemId"

class OverviewRecyclerAdapter(private val context: Context, val buildDetail: (Int) -> Unit) :
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
        val buildImage = binding.buildImage

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
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BuildViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            buildDetail(item.buildId)
        }
        when (item.category) {
            context.getString(R.string.arcane_build) -> {
                holder.buildImage.setImageResource(R.drawable.elden_class_arcane)
            }
            context.getString(R.string.faith_build) -> {
                holder.buildImage.setImageResource(R.drawable.elden_class_faith)
            }
            context.getString(R.string.intelligence_build) -> {
                holder.buildImage.setImageResource(R.drawable.elden_class_intelligence)
            }
            context.getString(R.string.strength_build) -> {
                holder.buildImage.setImageResource(R.drawable.elden_class_strength)
            }
            context.getString(R.string.dexterity_build) -> {
                holder.buildImage.setImageResource(R.drawable.elden_class_dexterity)
            }
        }
    }
}
