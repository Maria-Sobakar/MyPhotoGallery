package com.marias.myphotogallery.api

import com.marias.myphotogallery.data.PhotoItem
import com.marias.myphotogallery.data.UnsplashSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApiService {

    @GET("photos")
    suspend fun fetchPhotos(
        @Query("page") page: Int = 1
    ): Response<List<PhotoItem>>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") search: String, @Query("page") page: Int = 1
    ): Response<UnsplashSearchResponse>
}