package com.marias.myphotogallery.presentation.ui.photolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.marias.myphotogallery.data.PhotoItem
import com.marias.myphotogallery.data.repository.PhotoGalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoGalleryViewModel @Inject constructor(private val photoGalleryRepository: PhotoGalleryRepository) : ViewModel() {
    val photosLiveData = MutableLiveData<PagingData<PhotoItem>>()

    init {
        loadPhotos()
    }

    private fun fetchPhotos(): Flow<PagingData<PhotoItem>> {
        return photoGalleryRepository.fetchPhotos().cachedIn(viewModelScope)
    }

    private fun searchPhotos(search: String): Flow<PagingData<PhotoItem>> {
        return photoGalleryRepository.searchPhotos(search).cachedIn(viewModelScope)
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            fetchPhotos().collectLatest { pagingData ->
                photosLiveData.value = pagingData
            }
        }
    }

    fun loadSearchedPhotos(search: String) {
        viewModelScope.launch {
            viewModelScope.launch {
                searchPhotos(search).collectLatest { pagingData ->
                    photosLiveData.value = pagingData
                }
            }
        }
    }
}