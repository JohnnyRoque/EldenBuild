package com.eldenbuild.ui.build_detail_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.databinding.ItemSelectionGridBinding

class BuildItemsGridAdapter(val openItemDetail: (ItemsDefaultCategories) -> Unit) :
    ListAdapter<ItemsDefaultCategories, BuildItemsGridAdapter.BuildItemsViewHolder>(object :
        DiffUtil.ItemCallback<ItemsDefaultCategories>() {
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
    }) {
    class BuildItemsViewHolder(val binding: ItemSelectionGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val card = binding.cardItem

        fun bind(itemsDefaultCategories: ItemsDefaultCategories) {
            binding.apply {
                itemImage = itemsDefaultCategories
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildItemsViewHolder {
        return BuildItemsViewHolder(
            ItemSelectionGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BuildItemsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.card.setOnClickListener {
            openItemDetail(item)
        }
    }
}