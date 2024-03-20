package com.eldenbuild.ui.customize_build_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.eldenbuild.R
import com.eldenbuild.data.database.CharacterStatus
import com.eldenbuild.databinding.CustomizeBuildModalBottomSheetBinding
import com.eldenbuild.ui.build_detail_fragment.BuildDetailViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CustomizeBuildModalBottomSheet(

    private val confirmNewAttributes: (MutableList<CharacterStatus>) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var newStatusList: MutableSet<CharacterStatus>
    private var _binding: CustomizeBuildModalBottomSheetBinding? = null
    val binding get() = _binding!!
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
            lifecycleOwner = viewLifecycleOwner
            currentBuild = BuildDetailViewModel.CurrentBuild
        }

        lifecycleScope.launch {
            BuildDetailViewModel.buildDetail.filterNotNull()
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                newStatusList = it.buildCharacterStatus.toMutableSet()
            }
        }


        binding.editStatusRecyclerView.adapter = BuildStatusAdapter(true) { newAttr ->
            Log.d("bottomSheetLevel", " = $newAttr")
            for (i in newStatusList) {
                if (i.attributeName == newAttr.attributeName) {
                    i.attributeLevel = newAttr.attributeLevel
                }
            }
        }

        binding.buttonConfirmAttr.setOnClickListener {
            confirmNewAttributes(newStatusList.toMutableList())
            dismiss()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        Log.d("bottomSheet", " = ${viewLifecycleOwner.lifecycleScope.isActive}")
        super.onDestroyView()
    }
}