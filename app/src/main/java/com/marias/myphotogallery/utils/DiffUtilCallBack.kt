package com.marias.myphotogallery.utils

import androidx.recyclerview.widget.DiffUtil
import com.marias.myphotogallery.data.PhotoItem

class DiffUtilCallBack : DiffUtil.ItemCallback<PhotoItem>() {
    override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return oldItem.urls.thumb == newItem.urls.thumb
                && oldItem.urls.regular == newItem.urls.regular
    }
}