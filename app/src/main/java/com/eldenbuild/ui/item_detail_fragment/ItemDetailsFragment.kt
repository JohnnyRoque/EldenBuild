package com.eldenbuild.ui.item_detail_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.eldenbuild.R
import com.eldenbuild.databinding.FragmentItemDetailsBinding
import com.eldenbuild.util.Items
import com.eldenbuild.util.TypesOfStats
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

const val ITEM_FROM_BUILD = "isItemFromBuild"
const val TYPE = "type"

class ItemDetailsFragment : Fragment() {
    private var _binding: FragmentItemDetailsBinding? = null
    private var isItemFromBuild = false
    private val binding get() = _binding!!
    private val sharedViewMode: ItemDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            isItemFromBuild = it.getBoolean(ITEM_FROM_BUILD)

        }
        super.onCreate(savedInstanceState)
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

            if (!isItemFromBuild) {
                addToBuildButton.visibility = View.VISIBLE
            }
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
                    binding.categoryTextView.text = itemDetail.category
                    binding.weightTextView.text =
                        getString(R.string.weight_text, "${itemDetail.weight}")


                    when (arguments?.getString(TYPE)) {
                        Items.WEAPON -> {
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

                        Items.ARMOR -> {
                            amountAdapter.submitList(itemDetail.dmgNegation)
                            amountAdapter2.submitList(itemDetail.resistance)
                            binding.statsText.setText(TypesOfStats.damageNegation)
                            binding.statsText2.setText(TypesOfStats.resistance)
                            binding.divider4.visibility = View.GONE
                            binding.divider5.visibility = View.GONE

                        }

                        Items.SHIELD -> {
                            amountAdapter.submitList(itemDetail.attack)
                            amountAdapter2.submitList(itemDetail.defence)
                            scalingAdapter.submitList(itemDetail.scalesWith)
                            amountAdapter3.submitList(itemDetail.requiredAttributes)
                            binding.statsText.setText(TypesOfStats.attackPower)
                            binding.statsText2.setText(TypesOfStats.guardedDamage)
                            binding.statsText3.setText(TypesOfStats.attributesScaling)
                            binding.statsText4.setText(TypesOfStats.attributesRequired)
                        }

                        Items.TALISMANS -> {
                            binding.divider2.visibility = View.GONE
                            binding.divider3.visibility = View.GONE
                            binding.divider4.visibility = View.GONE
                            binding.divider5.visibility = View.GONE
                            binding.categoryTextView.text = getString(
                                R.string.effect_text,
                                itemDetail.effect
                            )
                        }

                        Items.SPIRITS -> {
                            binding.categoryTextView.text = getString(
                                R.string.effect_text,
                                itemDetail.effect
                            )

                            binding.weightTextView.text =
                                getString(
                                    R.string.hp_cost_and_fp_cost,
                                    "${itemDetail.hpCost}",
                                    "${itemDetail.fpCost}"
                                )

                            binding.divider2.visibility = View.GONE
                            binding.divider3.visibility = View.GONE
                            binding.divider4.visibility = View.GONE
                            binding.divider5.visibility = View.GONE


                        }

                        Items.ASHES_OF_WAR -> {
                            binding.categoryTextView.text = getString (
                                R.string.skill_text,
                                itemDetail.skill
                            )

                            binding.weightTextView.text =
                                getString(
                                    R.string.affinity_text,
                                    itemDetail.affinity
                                )

                            binding.divider2.visibility = View.GONE
                            binding.divider3.visibility = View.GONE
                            binding.divider4.visibility = View.GONE
                            binding.divider5.visibility = View.GONE
                        }

                            else -> {
                            binding.categoryTextView.text = getString(
                                R.string.effect_text,
                                itemDetail.effects
                            )
                            binding.statsText.setText(TypesOfStats.requires)


                            amountAdapter.submitList(itemDetail.requires)
                            binding.weightTextView.text =
                                getString(
                                    R.string.mp_cost_and_slots,
                                    "${itemDetail.cost}",
                                    "${itemDetail.slots}"
                                )
                            binding.divider4.visibility = View.GONE
                            binding.divider5.visibility = View.GONE
                            binding.divider3.visibility = View.GONE

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