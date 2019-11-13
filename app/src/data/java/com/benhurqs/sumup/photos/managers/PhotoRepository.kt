package com.benhurqs.sumup.photos.managers

import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.clients.local.PhotoLocalDataSource
import com.benhurqs.sumup.photos.clients.remote.PhotoRemoteDataSource
import com.benhurqs.sumup.photos.domains.entities.Photo
import com.benhurqs.sumup.user.clients.local.UserLocalDataSource
import com.benhurqs.sumup.user.managers.UserDataSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class PhotoRepository(val remoteDataSource: PhotoDataSource, val localDataSource: PhotoLocalDataSource) {


    companion object {
        private var mInstance: PhotoRepository? = null

        @Synchronized
        fun getInstance(remoteDataSource: PhotoDataSource, localDataSource: PhotoLocalDataSource): PhotoRepository {
            if(mInstance == null){
                mInstance = PhotoRepository(remoteDataSource, localDataSource)
            }
            return mInstance!!
        }
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
            .subscribe(object : Observer<List<Photo>?> {
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