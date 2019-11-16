package com.benhurqs.sumup.main.presentation.contracts

import com.benhurqs.sumup.main.domains.entities.Album

interface AlbumContract {
    interface View{
        fun loadingAlbums(albumList: List<Album>)
        fun showEmptyView()
        fun hideEmptyView()
        fun hideContent()
        fun showContent()
    }

    interface Presenter{
        fun callAlbumAPI(userID: Int?)
        fun retryCallAlbumAPI()
    }
}