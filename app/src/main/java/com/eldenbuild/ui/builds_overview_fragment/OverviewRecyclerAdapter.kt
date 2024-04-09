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
import com.google.android.material.card.MaterialCardView

const val TAG = "ItemId"

class OverviewRecyclerAdapter(
    private val context: Context,
    private var _position: Int = RecyclerView.NO_POSITION,
    val position: Int = _position,
    var checkedBuildList: List<BuildCategories> = listOf(),
    val checkedBuilds: ((MaterialCardView, BuildCategories, Boolean) -> Unit),
    val buildDetail: (Int) -> Unit
) :
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
        val card = binding.buildCard

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
        val card = holder.card

        if (checkedBuildList.isNotEmpty()) {
            for (i in 0..checkedBuildList.lastIndex) {
                card.isChecked = checkedBuildList.contains(item)
            }
        } else {
            card.isChecked = false
        }

        holder.itemView.setOnClickListener {
            when {
                !card.isChecked && checkedBuildList.isNotEmpty() -> {
                    checkedBuilds(card, item, true)

                }

                card.isChecked && checkedBuildList.isNotEmpty() -> {
                    checkedBuilds(card, item, false)
                }

                else -> {
                    buildDetail(item.buildId)
                }

            }
        }

        holder.itemView.setOnLongClickListener {
            this._position = holder.layoutPosition
            checkedBuilds(card, item, true)
            true
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
