package com.benhurqs.sumup.album.clients.remote

import com.benhurqs.sumup.album.managers.AlbumDataSource
import com.benhurqs.sumup.commons.api.SumUpAPIService
import com.benhurqs.sumup.photos.domains.entities.Album
import com.benhurqs.sumup.user.clients.remote.UserRemoteDataSource
import io.reactivex.Observable

class AlbumRemoteDataSource : AlbumDataSource{

    private val clientAPI: SumUpAPIService

    companion object {
        private var mInstance: AlbumRemoteDataSource? = null

        @Synchronized
        fun getInstance(): AlbumRemoteDataSource {
            if(mInstance == null){
                mInstance = AlbumRemoteDataSource()
            }
            return mInstance!!
        }
    }

    init {
        clientAPI = SumUpAPIService()
    }

    override fun getAlbumList(userID: Int): Observable<List<Album>> = clientAPI.getAlbums(userID)
}