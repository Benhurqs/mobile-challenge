package com.benhurqs.sumup.main.presentation.contracts

import com.benhurqs.sumup.main.domains.entities.Photo

interface PhotoContract {
    interface View{
        fun showLoading()
        fun hideLoading()
        fun showError()
        fun loadPhotos(photoList: List<Photo>)
        fun isAdded() : Boolean
        fun hideEmptyView()
        fun showEmptyView()
        fun hideError()
    }

    interface Presenter{
        fun callPhotoAPI(albumID: Int?)
    }
}