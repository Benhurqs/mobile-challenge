package com.benhurqs.sumup.photos.presentation.ui.contracts

import com.benhurqs.sumup.photos.domains.entities.Album

interface AlbumContract {
    interface View{
        fun loadingAlbums(albumList: List<Album>)
    }

    interface Presenter{
        fun callAlbumAPI(userID: Int?)
    }
}