package com.eldenbuild.ui.itemdetailfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eldenbuild.R
import com.eldenbuild.data.ItemArmors
import com.eldenbuild.data.ItemWeapons
import com.eldenbuild.databinding.FragmentItemDetailsBinding
import com.eldenbuild.util.TypesOfStats
import com.eldenbuild.viewmodel.OverViewViewModel
import kotlinx.coroutines.launch

class ItemDetailsFragment : Fragment() {
    private var _binding: FragmentItemDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewMode: OverViewViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_item_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val amountAdapter = StatsAmountAdapter(requireContext())
        val amountAdapter2 = StatsAmountAdapter(requireContext())
        val scalingAdapter = StatsScalingAdapter(requireContext())
        val amountAdapter3 = StatsAmountAdapter(requireContext())

        binding.addToBuildButton.setOnClickListener{
            sharedViewMode.itemDetail.value?.let {sharedViewMode.addItemToBuild(it)  }
        }




        binding.apply {
            viewModel = sharedViewMode
            binding.recyclerAmount2.adapter = amountAdapter2
            recyclerAmount1.adapter = amountAdapter
            recyclerAmount3.adapter = scalingAdapter
            recyclerAmount4.adapter = amountAdapter3
        }
        sharedViewMode.itemDetail.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                when (it) {
                    is ItemWeapons -> {
                        val weapon = it
                        amountAdapter.submitList(weapon.attack)
                        amountAdapter2.submitList(weapon.defence)
                        scalingAdapter.submitList(weapon.scalesWith)
                        amountAdapter3.submitList(weapon.requiredAttributes)
                        binding.divider4.visibility = View.VISIBLE
                        binding.divider5.visibility = View.VISIBLE
                        binding.statsText.setText(TypesOfStats.attackPower)
                        binding.statsText2.setText(TypesOfStats.guardedDamage)
                        binding.statsText3.setText(TypesOfStats.attributesScaling)
                        binding.statsText4.setText(TypesOfStats.attributesRequired)
                    }

                    else -> {
                        val armor = it as ItemArmors
                        amountAdapter.submitList(armor.dmgNegation)
                        amountAdapter2.submitList(armor.resistance)
                        binding.divider5.visibility = View.GONE
                        binding.divider4.visibility = View.GONE
                        binding.statsText.setText(TypesOfStats.damageNegation)
                        binding.statsText2.setText(TypesOfStats.resistance)
                    }
                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}