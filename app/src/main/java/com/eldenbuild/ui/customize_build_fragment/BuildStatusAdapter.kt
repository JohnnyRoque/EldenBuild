package com.eldenbuild.ui.customize_build_fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.data.CharacterStatus
import com.eldenbuild.databinding.BuildStatusHorizontalBinding

class BuildStatusAdapter(val setNewLevel: (String, Boolean) -> Unit) :
    ListAdapter<CharacterStatus, BuildStatusAdapter.BuildStatusViewHolder>(object :
        DiffUtil.ItemCallback<CharacterStatus>() {
        override fun areItemsTheSame(oldItem: CharacterStatus, newItem: CharacterStatus): Boolean {

            return oldItem.attributeName == newItem.attributeName
        }

        override fun areContentsTheSame(
            oldItem: CharacterStatus,
            newItem: CharacterStatus
        ): Boolean {
            return oldItem.newAttributeLevel == newItem.newAttributeLevel
        }
    }) {
        companion object{
            private var _itemPosition =0
            val itemPosition get() = _itemPosition
        }
    class BuildStatusViewHolder(val binding: BuildStatusHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val iconAdd = binding.iconAdd
        val iconMinus = binding.iconMinus
        fun bind(characterStatus: CharacterStatus) {
            binding.apply {
                attribute = characterStatus
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildStatusViewHolder {
        return BuildStatusViewHolder(
            BuildStatusHorizontalBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BuildStatusViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.iconAdd.setOnClickListener {
            _itemPosition = holder.adapterPosition
            setNewLevel(item.attributeName, true)
            Log.d(
                "Items", " Position = $position"
            )
        }

        holder.iconMinus.setOnClickListener {
            _itemPosition = holder.adapterPosition
            setNewLevel(item.attributeName, false)
            Log.d(
                "Items", " Layout Position = $position"
            )
        }
    }
}
