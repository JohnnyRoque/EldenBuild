package com.eldenbuild.ui.build_detail_fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.databinding.ItemSelectionGridBinding
import com.google.android.material.card.MaterialCardView

// Colocar um item como parametro para que seja adiciona a lista, retorno Unit?
//trocar os add por paramentro
class BuildItemsGridAdapter(
    private val isItemSelectable: Boolean,
    val checkedItems: ((MaterialCardView, ItemsDefaultCategories, Boolean) -> Unit),
    private val checkList: MutableList<ItemsDefaultCategories> = mutableListOf(),
    val openItemDetail: (ItemsDefaultCategories, String) -> Unit

) :
    ListAdapter<ItemsDefaultCategories, BuildItemsGridAdapter.BuildItemsViewHolder>(object :
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
        val card = holder.card

        if (checkList.isNotEmpty()) {
            for (i in (0..checkList.lastIndex)) {
                card.isChecked = checkList.contains(item)
            }
        } else {
            card.isChecked = false
        }

        holder.card.setOnClickListener {

            when {
                !card.isChecked && checkList.isNotEmpty() -> {
                    checkedItems(card, item, true)
                }

                card.isChecked && checkList.isNotEmpty() -> {
                    checkedItems(card, item, false)
                }

                else -> {
                    openItemDetail(item, item.itemType)
                    Log.d("ItemFrom", "Item is from = ${item.fromBuild}")
                }
            }
        }

        holder.card.setOnLongClickListener {
            if (checkList.isEmpty() && isItemSelectable) {
                checkedItems(card, item, true)
            }
            true

        }
    }
}