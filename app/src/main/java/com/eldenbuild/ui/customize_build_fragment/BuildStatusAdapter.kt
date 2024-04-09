package com.eldenbuild.ui.customize_build_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.data.database.CharacterStatus
import com.eldenbuild.databinding.BuildStatusHorizontalBinding

class BuildStatusAdapter(
    val isEditAttributes: Boolean,
    val setNewLevel: (CharacterStatus) -> Unit
) :
    ListAdapter<CharacterStatus, BuildStatusAdapter.BuildStatusViewHolder>(object :
        DiffUtil.ItemCallback<CharacterStatus>() {
        override fun areItemsTheSame(
            oldItem: CharacterStatus,
            newItem: CharacterStatus
        ): Boolean {
            return oldItem.attributeName == newItem.attributeName
        }

        override fun areContentsTheSame(
            oldItem: CharacterStatus,
            newItem: CharacterStatus
        ): Boolean {
            return oldItem.attributeLevel == newItem.attributeLevel
        }
    }) {


    class BuildStatusViewHolder(val binding: BuildStatusHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(characterStatus: CharacterStatus) {

            binding.apply {
                attribute = characterStatus
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildStatusViewHolder {
        return BuildStatusViewHolder (
            BuildStatusHorizontalBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BuildStatusViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.binding.editAttributeLevel.doAfterTextChanged {
                if (!it.isNullOrBlank()) {
                    val newValue = CharacterStatus(item.attributeName,it.toString().toInt())
                    setNewLevel(newValue)
                }


        }

        if (isEditAttributes) {
            holder.binding.editAttributeLevelLayout.visibility = View.VISIBLE
            holder.binding.attributeLevel.visibility = View.INVISIBLE
        } else{
            holder.binding.editAttributeLevelLayout.visibility = View.INVISIBLE
            holder.binding.attributeLevel.visibility =View.VISIBLE
        }
    }
}
