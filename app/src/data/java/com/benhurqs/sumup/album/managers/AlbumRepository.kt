package com.benhurqs.sumup.album.managers

import com.benhurqs.sumup.album.clients.local.AlbumLocalDataSource
import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.Album
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

open class AlbumRepository(val remoteDataSource: AlbumDataSource, val localDataSource: AlbumLocalDataSource) {

    companion object {
        private var mInstance: AlbumRepository? = null

        @Synchronized
        fun getInstance(remoteDataSource: AlbumDataSource, localDataSource: AlbumLocalDataSource): AlbumRepository {
            if(mInstance == null){
                mInstance = AlbumRepository(remoteDataSource, localDataSource)
            }
            return mInstance!!
        }
    }

    fun getAlbumList(userID: Int, callback: APICallback<List<Album>?>){
        remoteDataSource.getAlbumList(userID)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                callback.onStart()
            }
            .subscribe(object : Observer<List<Album>?> {
                override fun onError(e: Throwable?) {
                    callback.onError()
                }

                override fun onNext(value: List<Album>?) {
                    callback.onSuccess(value)
                }

                override fun onComplete() {
                    callback.onFinish()
                }

                override fun onSubscribe(d: Disposable?) {}

            })
    }
}