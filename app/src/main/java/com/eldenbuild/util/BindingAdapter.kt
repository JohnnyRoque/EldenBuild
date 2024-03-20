package com.eldenbuild.util

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.eldenbuild.R
import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.data.database.CharacterStatus
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.ui.build_detail_fragment.BuildItemsGridAdapter
import com.eldenbuild.ui.builds_overview_fragment.OverviewRecyclerAdapter
import com.eldenbuild.ui.customize_build_fragment.BuildStatusAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("imageUrl")
fun bindingImage(imageView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imageUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imageView.load(imageUri) {
            error(R.drawable.ic_broken_image)
        }
    }
}


@BindingAdapter("statusList")
fun bindStatusLisToRecyclerView(recyclerView: RecyclerView, data: List<CharacterStatus>?) {
    val adapter = recyclerView.adapter as BuildStatusAdapter
    adapter.submitList(data)


}

@BindingAdapter("itemList")
fun bindItemListToRecyclerView(
    recyclerView: RecyclerView,
    data: List<ItemsDefaultCategories>?
) {
    try {
        val adapter = recyclerView.adapter as BuildItemsGridAdapter
        adapter.submitList(data)
    } catch (e: NullPointerException) {
        Log.d("NullAdapter", "$e")
    }
}

@BindingAdapter("imageInt")
fun setImageInt(imageView: ImageView, image: Int) {
    imageView.setImageResource(image)
}

@BindingAdapter("intToString")
fun bindIntToString(textView: TextView, intText: Int) {
    textView.text = intText.toString()
}


@BindingAdapter("int_to_string_placeHolder")
fun bindIntToStringPlaceholder(textInputLayout: TextInputLayout, intText: Int) {
    textInputLayout.placeholderText = intText.toString()
}

@BindingAdapter("buildList")
fun bindBuildListToRecyclerView(
    recyclerView: RecyclerView,
    data: List<BuildCategories>?
) {
    try {
        val adapter = recyclerView.adapter as OverviewRecyclerAdapter
        adapter.submitList(data)
    } catch (e: NullPointerException) {
        Log.d("NullAdapter", "$e")
    }

}








