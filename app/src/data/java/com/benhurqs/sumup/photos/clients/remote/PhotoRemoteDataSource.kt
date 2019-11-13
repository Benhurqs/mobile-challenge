package com.benhurqs.sumup.photos.clients.remote

import com.benhurqs.sumup.commons.api.SumUpAPIService
import com.benhurqs.sumup.photos.domains.entities.Photo
import com.benhurqs.sumup.photos.managers.PhotoDataSource
import io.reactivex.Observable

class PhotoRemoteDataSource: PhotoDataSource {

    private val clientAPI: SumUpAPIService

    companion object {
        private var mInstance: PhotoRemoteDataSource? = null

        @Synchronized
        fun getInstance(): PhotoRemoteDataSource {
            if(mInstance == null){
                mInstance = PhotoRemoteDataSource()
            }
            return mInstance!!
        }
    }

    init {
        clientAPI = SumUpAPIService()
    }

    override fun getPhotoList(albumID: Int): Observable<List<Photo>> = clientAPI.getPhotos(albumID)
}