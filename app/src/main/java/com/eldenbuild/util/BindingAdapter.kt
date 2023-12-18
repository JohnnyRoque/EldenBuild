package com.eldenbuild.util

import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.eldenbuild.R
import com.eldenbuild.data.BuildCategories
import com.eldenbuild.data.ItemsDefaultCategories
import com.eldenbuild.ui.builds_overview_fragment.OverviewRecyclerAdapter
import com.google.android.material.textfield.MaterialAutoCompleteTextView

@BindingAdapter("imageUrl")
fun bindingImage(imageView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imageUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imageView.load(imageUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("buildList")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<BuildCategories>?) {
    val adapter = recyclerView.adapter as OverviewRecyclerAdapter
    adapter.submitList(data)
}

@BindingAdapter("itemList")
fun bindItemListToRecyclerView(recyclerView: RecyclerView, data: List<ItemsDefaultCategories>) {
    val adapter = recyclerView.adapter
}

@BindingAdapter("menuList")
fun bindListToMenu(autoCompleteTextView: AutoCompleteTextView, list: Array<String>) {
    (autoCompleteTextView as MaterialAutoCompleteTextView).setSimpleItems(list)
}

@BindingAdapter("imageInt")
fun setImageInt(imageView: ImageView,image:Int){
    imageView.setImageResource(image)
}








