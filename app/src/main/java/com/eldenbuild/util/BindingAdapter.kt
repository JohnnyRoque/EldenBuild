package com.eldenbuild.util

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.eldenbuild.data.ItemsDefaultCategories
import com.eldenbuild.ui.builds_overview_fragment.OverviewRecyclerAdapter

@BindingAdapter("imageUrl")
fun bindingImage(imageView:ImageView,imgUrl:String?){
    imgUrl?.let {
        val imageUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imageView.load(imageUri)
    }
}
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,data: List<ItemsDefaultCategories>?){
    val adapter = recyclerView.adapter as OverviewRecyclerAdapter
    adapter.submitList(data)

}

