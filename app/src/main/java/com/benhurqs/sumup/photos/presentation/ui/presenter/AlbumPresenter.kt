package com.benhurqs.sumup.photos.presentation.ui.presenter

import com.benhurqs.sumup.album.managers.AlbumRepository
import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.Album
import com.benhurqs.sumup.photos.presentation.ui.contracts.AlbumContract
import com.benhurqs.sumup.photos.presentation.ui.contracts.MainView

class AlbumPresenter(private val albumView: AlbumContract.View, private val mainView: MainView, private val albumRepository: AlbumRepository): AlbumContract.Presenter,
    APICallback<List<Album>, String> {

    override fun callAlbumAPI(userID: Int?) {
        albumRepository.getAlbumList(userID, this)
    }

    override fun onStart() {
        if(mainView.isAdded()){
            mainView.showLoading()
        }
    }

    override fun onError(error: String) {
        if(mainView.isAdded()){
            mainView.showError(error)
        }
    }

    override fun onFinish() {
        if(mainView.isAdded()){
            mainView.hideLoading()
        }
    }

    override fun onSuccess(response: List<Album>) {
        if(mainView.isAdded()){
            albumView.loadingAlbums(response)
        }
    }
}