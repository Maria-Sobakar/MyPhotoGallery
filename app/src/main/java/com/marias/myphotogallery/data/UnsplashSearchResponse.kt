package com.marias.myphotogallery.data

import com.google.gson.annotations.SerializedName

class UnsplashSearchResponse(
    val results: List<PhotoItem>,
    @SerializedName("total_pages") val pages: Int
)