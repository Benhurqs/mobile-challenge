package com.benhurqs.sumup.album.managers

import android.util.SparseArray
import com.benhurqs.sumup.album.clients.local.AlbumLocalDataSource
import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.Album
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

open class AlbumRepository(val remoteDataSource: AlbumDataSource, val localDataSource: AlbumLocalDataSource) {

    private var cachedAlbumList = SparseArray<List<Album>>()

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
        var albumList = cachedAlbumList.get(userID)
        if(albumList.isNullOrEmpty()){
            callLocalDatabase(userID, callback)
        }else{
            callback.onStart()
            callback.onSuccess(albumList)
            callback.onFinish()
        }
    }

    private fun callRemoteAPI(userID: Int, callback: APICallback<List<Album>?>? = null){
        remoteDataSource.getAlbumList(userID)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                callback?.onStart()
            }
            .subscribe(object : Observer<List<Album>?> {
                override fun onError(e: Throwable?) {
                    callback?.onError()
                }

                override fun onNext(list: List<Album>?) {
                    callback?.onSuccess(list)
                    if(!list.isNullOrEmpty()){
                        saveAlbums(userID, list)
                    }
                }

                override fun onComplete() {
                    callback?.onFinish()
                }

                override fun onSubscribe(d: Disposable?) {}

            })

    }


    private fun callLocalDatabase(userID: Int, callback: APICallback<List<Album>?>){
       localDataSource.getAlbumList(userID)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                callback.onStart()
            }
            .subscribe(object : Observer<List<Album>?> {
                override fun onError(e: Throwable?) {
                    callRemoteAPI(userID, callback)
                }

                override fun onNext(list: List<Album>?) {
                    if(list.isNullOrEmpty()){
                        callRemoteAPI(userID, callback)
                    }else{
                        callback.onSuccess(list)
                        callback.onFinish()

                        callRemoteAPI(userID)
                    }
                }

                override fun onComplete() {}

                override fun onSubscribe(d: Disposable?) {}

            })
    }

    private fun saveAlbums(userId: Int, list: List<Album>){
        cachedAlbumList.put(userId, list)
        list.forEach { album ->
            localDataSource.saveAlbums(album)
        }
    }
}