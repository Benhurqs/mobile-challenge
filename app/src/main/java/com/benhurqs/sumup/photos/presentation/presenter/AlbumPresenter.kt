package com.benhurqs.sumup.photos.presentation.presenter

import com.benhurqs.sumup.album.managers.AlbumRepository
import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.Album
import com.benhurqs.sumup.photos.presentation.contracts.AlbumContract
import com.benhurqs.sumup.photos.presentation.contracts.MainView

class AlbumPresenter(private val albumView: AlbumContract.View, private val mainView: MainView, private val albumRepository: AlbumRepository): AlbumContract.Presenter,
    APICallback<List<Album>?> {

    override fun callAlbumAPI(userID: Int?) {
        if(userID == null){
            onError()
            return
        }
        albumRepository.getAlbumList(userID, this)
    }

    override fun onStart() {
        if(mainView.isAdded()){
            mainView.showLoading()
            albumView.hideEmptyView()
        }
    }

    override fun onError() {
        if(mainView.isAdded()){
            mainView.showError()
        }
    }

    override fun onFinish() {
        if(mainView.isAdded()){
            mainView.hideLoading()
        }
    }

    override fun onSuccess(albumList: List<Album>?) {
        if(mainView.isAdded()){
            if(albumList.isNullOrEmpty()){
                albumView.showEmptyView()
            }else{
                albumView.loadingAlbums(albumList)
            }


        }
    }
}