package com.eldenbuild.ui.builds_overview_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eldenbuild.R
import com.eldenbuild.databinding.FragmentBuildsOverviewBinding
import com.eldenbuild.util.Items
import com.eldenbuild.viewmodel.OverViewViewModel

class BuildsOverviewFragment : Fragment() {
    private var _binding: FragmentBuildsOverviewBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: OverViewViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_builds_overview, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            armor = Items.ARMOR
            weapon = Items.WEAPON

        }
        binding.itemSelectionGridRecycler.adapter = OverviewRecyclerAdapter {id: String, position: Int ->

                sharedViewModel.showItemDetail(id)
                Log.d(TAG,position.toString())
                findNavController().navigate(BuildsOverviewFragmentDirections.actionBuildsOverviewFragmentToItemDetailsFragment())
            }
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}