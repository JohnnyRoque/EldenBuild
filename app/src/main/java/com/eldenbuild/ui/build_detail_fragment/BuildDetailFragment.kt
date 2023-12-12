package com.eldenbuild.ui.build_detail_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.eldenbuild.R
import com.eldenbuild.databinding.FragmentBuildDetailBinding
import com.google.android.material.carousel.CarouselSnapHelper


class BuildDetailFragment : Fragment() {
    private var _binding: FragmentBuildDetailBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_build_detail,
                container,
                false
            )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {

        }
        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(binding.itemSelectionCarousel)
        super.onViewCreated(view, savedInstanceState)
    }

}