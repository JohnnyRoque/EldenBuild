package com.eldenbuild.ui.builds_overview_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.R
import com.eldenbuild.data.ItemsDefaultCategories
import com.eldenbuild.databinding.ItemSelectionGridBinding


class OverviewRecyclerAdapter :
    ListAdapter<ItemsDefaultCategories, OverviewRecyclerAdapter.OverViewRecyclerViewHolder>(
        DiffCallback
    ) {

    class OverViewRecyclerViewHolder(private var binding:ItemSelectionGridBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemsDefaultCategories: ItemsDefaultCategories){
            binding.itemImage = itemsDefaultCategories
            binding.executePendingBindings()
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<ItemsDefaultCategories>() {
        override fun areItemsTheSame(
            oldItem: ItemsDefaultCategories,
            newItem: ItemsDefaultCategories
        ): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ItemsDefaultCategories,
            newItem: ItemsDefaultCategories
        ): Boolean {

            return oldItem.image == newItem.image
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverViewRecyclerViewHolder {
        return OverViewRecyclerViewHolder(ItemSelectionGridBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: OverViewRecyclerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}