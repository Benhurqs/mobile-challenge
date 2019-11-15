package com.benhurqs.sumup.photos.presentation.contracts

import com.benhurqs.sumup.photos.domains.entities.Photo

interface PhotoContract {
    interface View{
        fun showLoading()
        fun hideLoading()
        fun showError()
        fun loadPhotos(photoList: List<Photo>)
        fun isAdded() : Boolean
    }

    interface Presenter{
        fun callPhotoAPI(albumID: Int)
    }
}