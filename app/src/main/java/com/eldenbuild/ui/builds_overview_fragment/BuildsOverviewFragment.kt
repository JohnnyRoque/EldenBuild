package com.eldenbuild.ui.builds_overview_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.eldenbuild.R
import com.eldenbuild.databinding.FragmentBuildsOverviewBinding
import com.eldenbuild.ui.SlidingPaneOnBackPressedCallback
import com.eldenbuild.util.AppViewModelProvider
import com.eldenbuild.util.Dialog
import com.eldenbuild.viewmodel.OverViewViewModel

class BuildsOverviewFragment : Fragment() {


    private var _binding: FragmentBuildsOverviewBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: OverViewViewModel by activityViewModels{
        AppViewModelProvider.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_builds_overview, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            SlidingPaneOnBackPressedCallback(binding.slidingPaneLayout)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = OverviewRecyclerAdapter(requireContext()){
            sharedViewModel.showBuildDetail(it)
            binding.detailNavHost.visibility = View.VISIBLE
            if (!binding.slidingPaneLayout.isOpen) {
                binding.slidingPaneLayout.openPane()
            }
        }


        if (sharedViewModel.buildsList.value.isNullOrEmpty()) {
            binding.detailNavHost.visibility = View.GONE
        }

        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        binding.slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED
        binding.addBuildFab.setOnClickListener {
            Dialog.buildCustomDialog(requireContext(), layoutInflater) { name, type, description ->
                sharedViewModel.createNewBuild(name, type, description)
            }
        }
        binding.buildRecyclerView.adapter = adapter

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}