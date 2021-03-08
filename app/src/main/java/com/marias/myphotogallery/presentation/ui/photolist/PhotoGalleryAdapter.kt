package com.marias.myphotogallery.presentation.ui.photolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marias.myphotogallery.utils.DiffUtilCallBack
import com.marias.myphotogallery.data.PhotoItem
import com.marias.myphotogallery.R
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PhotoGalleryAdapter @Inject constructor(val activity: FragmentActivity) :
    PagingDataAdapter<PhotoItem, PhotoGalleryAdapter.PhotoGalleryViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoGalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return PhotoGalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoGalleryViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class PhotoGalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PhotoItem) {
            val imageView = itemView.findViewById<ImageView>(R.id.photoImageView)
            Glide.with(activity)
                .load(item.urls.thumb)
                .into(imageView)

            imageView.setOnClickListener {
                val navController = Navigation.findNavController(
                    activity,
                    R.id.navigation_host_fragment
                )
                val arg = Bundle().apply {
                    putString(FULLSCREEN_URL_KEY, item.urls.regular)
                }
                navController.navigate(R.id.action_to_fullscreen, arg)
            }
        }
    }

    companion object {
        const val FULLSCREEN_URL_KEY = "fullScreenPhoto"
    }
}