package com.marias.myphotogallery.api.paging

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.android.material.snackbar.Snackbar
import com.marias.myphotogallery.R
import com.marias.myphotogallery.data.PhotoItem
import com.marias.myphotogallery.api.UnsplashApiService

import retrofit2.HttpException
import java.io.IOException

class PhotoGalleryPagingSource(
    private val service: UnsplashApiService
) :
    PagingSource<Int, PhotoItem>() {
    private var pageNumber = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoItem> {
        return try {
            val response = service.fetchPhotos(pageNumber)
            val photos = response.body()
            LoadResult.Page(
                photos ?: listOf(),
                null,
                ++pageNumber
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PhotoItem>): Int {
        return pageNumber
    }
}