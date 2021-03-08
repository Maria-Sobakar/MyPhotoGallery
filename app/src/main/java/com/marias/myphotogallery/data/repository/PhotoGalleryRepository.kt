package com.marias.myphotogallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.marias.myphotogallery.api.UnsplashApiService
import com.marias.myphotogallery.api.paging.PhotoGalleryPagingSource
import com.marias.myphotogallery.api.paging.PhotoGallerySearchPagingSource
import com.marias.myphotogallery.data.PhotoItem
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class PhotoGalleryRepository @Inject constructor(val service: UnsplashApiService) {

    fun fetchPhotos(): Flow<PagingData<PhotoItem>> {
        return Pager(
            PagingConfig(30, enablePlaceholders = false)
        ) {
            PhotoGalleryPagingSource(service)
        }.flow
    }

    fun searchPhotos(search: String): Flow<PagingData<PhotoItem>> {
        return Pager(
            PagingConfig(30, enablePlaceholders = false)
        ) {
            PhotoGallerySearchPagingSource(service, search)
        }.flow
    }
}