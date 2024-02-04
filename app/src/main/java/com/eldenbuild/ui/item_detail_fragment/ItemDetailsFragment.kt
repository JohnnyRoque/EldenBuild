package com.eldenbuild.ui.item_detail_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.eldenbuild.R
import com.eldenbuild.databinding.FragmentItemDetailsBinding
import com.eldenbuild.util.AppViewModelProvider
import com.eldenbuild.util.TypesOfStats
import kotlinx.coroutines.launch

class ItemDetailsFragment : Fragment() {
    private var _binding: FragmentItemDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewMode: ItemDetailViewModel by activityViewModels {
        AppViewModelProvider.Factory
    }


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


        binding.apply {
            viewModel = sharedViewMode
            binding.recyclerAmount2.adapter = amountAdapter2
            recyclerAmount1.adapter = amountAdapter
            recyclerAmount3.adapter = scalingAdapter
            recyclerAmount4.adapter = amountAdapter3
        }


        lifecycleScope.launch {
            sharedViewMode.itemDetail.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { itemDetail ->
                    binding.addToBuildButton.setOnClickListener {
                        Toast.makeText(
                            requireContext(),
                            sharedViewMode.addItemToBuild(itemDetail),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }


                    amountAdapter.submitList(itemDetail.attack)
                    amountAdapter2.submitList(itemDetail.defence)
                    scalingAdapter.submitList(itemDetail.scalesWith)
                    amountAdapter3.submitList(itemDetail.requiredAttributes)
                    binding.divider4.visibility = View.VISIBLE
                    binding.divider5.visibility = View.VISIBLE
                    binding.statsText.setText(TypesOfStats.attackPower)
                    binding.statsText2.setText(TypesOfStats.guardedDamage)
                    binding.statsText3.setText(TypesOfStats.attributesScaling)
                    binding.statsText4.setText(TypesOfStats.attributesRequired)
                }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}