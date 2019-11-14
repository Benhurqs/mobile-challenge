package com.benhurqs.sumup.photos.presentation.ui.contracts

import android.provider.Contacts
import com.benhurqs.sumup.photos.domains.entities.Album
import com.benhurqs.sumup.photos.domains.entities.Photo

interface PhotoContract {
    interface View{
        fun showLoading()
        fun hideLoading()
        fun showError(error: String)
        fun loadPhotos(photoList: List<Photo>)
        fun isAdded() : Boolean
    }

    interface Presenter{
        fun callPhotoAPI(albumID: Int)
    }
}