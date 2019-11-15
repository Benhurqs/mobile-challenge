package com.benhurqs.sumup.photos.presentation.presenter

import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.Photo
import com.benhurqs.sumup.photos.managers.PhotoRepository
import com.benhurqs.sumup.photos.presentation.contracts.PhotoContract

class PhotoPresenter(private val view: PhotoContract.View, private val repository: PhotoRepository): PhotoContract.Presenter,
    APICallback<List<Photo>?> {
    override fun callPhotoAPI(albumID: Int) {
        repository.getPhotoList(albumID, this)
    }

    override fun onStart() {
        if(view.isAdded()){
            view.showLoading()
        }
    }

    override fun onError() {
        if(view.isAdded()){
            view.showError()
        }
    }

    override fun onFinish() {
        if(view.isAdded()){
            view.hideLoading()
        }
    }

    override fun onSuccess(photoList: List<Photo>?) {
        if(view.isAdded()){
            if(photoList.isNullOrEmpty()){

            }else{
                view.loadPhotos(photoList)
            }

        }
    }
}