package com.eldenbuild.ui.build_detail_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.R
import com.eldenbuild.databinding.ItemSelectionCarouselBinding

class CarouselAdapter(val navToCustomizeBuild: (String) -> Unit) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    private val listOfImages =
        listOf(
            ListOfImagesCarousel(R.drawable.eldenring_1, "Equipment"),
            ListOfImagesCarousel(R.drawable.eldenring_3, "Status"),
            ListOfImagesCarousel(R.drawable.eldenring_4, "Magic"),
            )

    class CarouselViewHolder(val binding: ItemSelectionCarouselBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.carouselImage

        fun bind(listOfImagesCarousel: ListOfImagesCarousel) {
            binding.apply {
                imagesB = listOfImagesCarousel
                executePendingBindings()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        return CarouselViewHolder(
            ItemSelectionCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listOfImages.size
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = listOfImages[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            navToCustomizeBuild(item.text)
        }
    }
}
data class ListOfImagesCarousel(val image: Int, val text: String)

