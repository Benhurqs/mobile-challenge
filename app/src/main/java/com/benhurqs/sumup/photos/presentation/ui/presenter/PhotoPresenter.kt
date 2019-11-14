package com.benhurqs.sumup.photos.presentation.ui.presenter

import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.Photo
import com.benhurqs.sumup.photos.managers.PhotoRepository
import com.benhurqs.sumup.photos.presentation.ui.contracts.PhotoContract

class PhotoPresenter(private val view: PhotoContract.View, private val repository: PhotoRepository): PhotoContract.Presenter,
    APICallback<List<Photo>, String> {
    override fun callPhotoAPI(albumID: Int) {
        repository.getPhotoList(albumID, this)
    }

    override fun onStart() {
        if(view.isAdded()){
            view.showLoading()
        }
    }

    override fun onError(error: String) {
        if(view.isAdded()){
            view.showError(error)
        }
    }

    override fun onFinish() {
        if(view.isAdded()){
            view.hideLoading()
        }
    }

    override fun onSuccess(response: List<Photo>) {
        if(view.isAdded()){
            view.loadPhotos(response)
        }
    }
}