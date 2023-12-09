package com.eldenbuild.ui.builds_overview_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.data.ItemsDefaultCategories
import com.eldenbuild.databinding.ItemSelectionGridBinding
import com.google.android.material.card.MaterialCardView

const val TAG = "ItemId"
class OverviewRecyclerAdapter(val itemDetail: (String,Int) -> Unit) :
    ListAdapter<ItemsDefaultCategories, OverviewRecyclerAdapter.OverViewRecyclerViewHolder>(
        DiffCallback
    ) {

    class OverViewRecyclerViewHolder(private var binding: ItemSelectionGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val card : MaterialCardView = binding.cardItem
        fun bind(itemsDefaultCategories: ItemsDefaultCategories) {
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
        return OverViewRecyclerViewHolder(
            ItemSelectionGridBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: OverViewRecyclerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.card.setOnClickListener{
            itemDetail(item.id,item.hashCode())

        }
    }
}