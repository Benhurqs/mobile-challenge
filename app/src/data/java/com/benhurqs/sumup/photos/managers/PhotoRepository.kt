package com.benhurqs.sumup.photos.managers

import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.clients.remote.PhotoRemoteDataSource
import com.benhurqs.sumup.photos.domains.entities.Photo
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class PhotoRepository {

    private val remoteDataSource: PhotoDataSource
//    val localDataSource: PhotoDataSource

    companion object {
        private var mInstance: PhotoRepository? = null

        @Synchronized
        fun getInstance(): PhotoRepository {
            if(mInstance == null){
                mInstance = PhotoRepository()
            }
            return mInstance!!
        }
    }

    init {
        remoteDataSource = PhotoRemoteDataSource.getInstance()
    }

    fun getPhotoList(albumID: Int?, callback: APICallback<List<Photo>, String>){
        if(albumID == null){
            callback.onError("")
            return
        }

        remoteDataSource.getPhotoList(albumID)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                callback.onStart()
            }
            .subscribe(object : Observer<List<Photo>> {
                override fun onError(e: Throwable?) {
                    if(e?.message != null){
                        callback.onError(e.message!!)
                    }
                }

                override fun onNext(value: List<Photo>?) {
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