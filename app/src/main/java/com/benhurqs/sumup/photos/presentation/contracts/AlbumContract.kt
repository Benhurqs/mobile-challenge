package com.benhurqs.sumup.photos.presentation.contracts

import com.benhurqs.sumup.photos.domains.entities.Album

interface AlbumContract {
    interface View{
        fun loadingAlbums(albumList: List<Album>)
        fun showEmptyView()
        fun hideEmptyView()
    }

    interface Presenter{
        fun callAlbumAPI(userID: Int?)
    }
}