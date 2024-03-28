package com.eldenbuild.ui.customize_build_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.databinding.ItemSelectionGridBinding

class ItemsGridAdapter(
    val openItemDetail: (ItemsDefaultCategories, String) -> Unit

) : PagingDataAdapter<ItemsDefaultCategories, ItemsGridAdapter.ItemsViewHolder>(object :
    DiffUtil.ItemCallback<ItemsDefaultCategories>() {

    override fun areItemsTheSame(
        oldItem: ItemsDefaultCategories,
        newItem: ItemsDefaultCategories
    ): Boolean {
        return oldItem.id == newItem.id && oldItem.fromBuild == newItem.fromBuild
    }

    override fun areContentsTheSame(
        oldItem: ItemsDefaultCategories,
        newItem: ItemsDefaultCategories
    ): Boolean {
        return oldItem.image == newItem.image && oldItem.name == newItem.name
    }

}) {
    class ItemsViewHolder(val binding: ItemSelectionGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemsDefaultCategories: ItemsDefaultCategories) {
            binding.apply {
                item = itemsDefaultCategories
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        return ItemsViewHolder(
            ItemSelectionGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(item)
            holder.binding.cardItem.isCheckable = false

            holder.binding.cardItem.setOnClickListener {
                openItemDetail(item, item.itemType)
            }
        }
    }
}