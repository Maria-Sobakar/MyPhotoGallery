package com.marias.myphotogallery.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.marias.myphotogallery.data.PhotoItem
import com.marias.myphotogallery.api.UnsplashApiService
import retrofit2.HttpException
import java.io.IOException

class PhotoGallerySearchPagingSource (
    private val service: UnsplashApiService,
    private val query:String
) :
    PagingSource<Int, PhotoItem>() {
    private var pageNumber = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoItem> {
        return try {
            val response = service.searchPhotos(query,pageNumber)
            val photos = response.body()?.results
            val totalPages = response.body()?.pages?:1
            val nextPageNumber = if (pageNumber<totalPages) ++pageNumber else null
            LoadResult.Page(
                photos ?: listOf(),
                null,
                nextPageNumber
            )

        } catch (exception: IOException) { // 6
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PhotoItem>): Int {
        return pageNumber
    }
}