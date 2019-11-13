package com.benhurqs.sumup.album.managers

import com.benhurqs.sumup.album.clients.remote.AlbumRemoteDataSource
import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.Album
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AlbumRepository {

    private val remoteDataSource: AlbumDataSource
//    val localDataSource: AlbumDataSource

    companion object {
        private var mInstance: AlbumRepository? = null

        @Synchronized
        fun getInstance(): AlbumRepository {
            if(mInstance == null){
                mInstance = AlbumRepository()
            }
            return mInstance!!
        }
    }

    init {
        remoteDataSource = AlbumRemoteDataSource.getInstance()
    }

    fun getAlbumList(userID: Int?, callback: APICallback<List<Album>, String>){
        if(userID == null){
            callback.onError("")
            return
        }

        remoteDataSource.getAlbumList(userID)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                callback.onStart()
            }
            .subscribe(object : Observer<List<Album>> {
                override fun onError(e: Throwable?) {
                    if(e?.message != null){
                        callback.onError(e.message!!)
                    }
                }

                override fun onNext(value: List<Album>?) {
                    if(value.isNullOrEmpty()){
                        callback.onError("")
                    }else{
                        callback.onSuccess(value)
                    }
                }

                override fun onComplete() {
                    callback.onFinish()
                }

                override fun onSubscribe(d: Disposable?) {}

            })
    }
}