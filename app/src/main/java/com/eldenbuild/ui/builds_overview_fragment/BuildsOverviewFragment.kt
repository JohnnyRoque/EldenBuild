package com.eldenbuild.ui.builds_overview_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.eldenbuild.R
import com.eldenbuild.databinding.FragmentBuildsOverviewBinding
import com.eldenbuild.ui.SlidingPaneOnBackPressedCallback
import com.eldenbuild.ui.build_detail_fragment.BuildDetailViewModel.CurrentBuild
import com.eldenbuild.util.AppViewModelProvider
import com.eldenbuild.util.Dialog
import com.eldenbuild.viewmodel.OverViewViewModel
import kotlinx.coroutines.launch

const val BUILD_ID = "buildId"

class BuildsOverviewFragment : Fragment() {
    private var _binding: FragmentBuildsOverviewBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: OverViewViewModel by activityViewModels {
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

        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        val adapter = OverviewRecyclerAdapter(requireContext()) { id ->
            if (!binding.slidingPaneLayout.isOpen) {
                binding.slidingPaneLayout.openPane()
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    // Returns the build id on click
                    launch {
                        sharedViewModel.getCurrentBuild(id).collect { build ->
                            CurrentBuild.getBuildDetail(build)
                            Log.d(TAG, "${CurrentBuild.buildDetail.value}")
                        }
                    }
                }
            }
        }

        //Submit new list of Builds
        lifecycleScope.launch {
            sharedViewModel.buildsList.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    adapter.submitList(it)
                    if (it.isNotEmpty()) {
                        binding.detailNavHost.visibility = View.VISIBLE
                    }
                }
        }

        binding.buildRecyclerView.adapter = adapter

        binding.slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED
        binding.addBuildFab.setOnClickListener {
            Dialog.buildCustomDialog(requireContext(), layoutInflater) { name, type, description ->
                sharedViewModel.createNewBuild(name, type, description)

            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
