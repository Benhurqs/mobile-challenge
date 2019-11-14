package com.benhurqs.sumup.photos.presentation.ui.contracts

import android.provider.Contacts
import com.benhurqs.sumup.photos.domains.entities.Album

interface PhotoContract {
    interface View{
        fun showLoading()
        fun loadAlbuns(albumList: List<Album>)
    }

    interface Presenter{

    }
}