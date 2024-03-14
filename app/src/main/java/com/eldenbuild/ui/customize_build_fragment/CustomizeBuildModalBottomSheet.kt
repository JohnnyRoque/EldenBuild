package com.eldenbuild.ui.customize_build_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.eldenbuild.R
import com.eldenbuild.databinding.CustomizeBuildModalBottomSheetBinding
import com.eldenbuild.ui.build_detail_fragment.BuildDetailViewModel
import com.eldenbuild.viewmodel.OverViewViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomizeBuildModalBottomSheet : BottomSheetDialogFragment() {
    private var _binding: CustomizeBuildModalBottomSheetBinding? = null
    val binding get() = _binding!!
    private val sharedViewModel : OverViewViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.customize_build_modal_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            lifecycleOwner =viewLifecycleOwner
            currentBuild = BuildDetailViewModel.CurrentBuild
        }
//        binding.editStatusRecyclerView.adapter = BuildStatusAdapter(true){attName, isInc, ->
//            sharedViewModel.setNewAttribute(attName, isInc)
//        }

        binding.buttonConfirmAttr.setOnClickListener {
            dismiss()
        }
        super.onViewCreated(view, savedInstanceState)
    }
}