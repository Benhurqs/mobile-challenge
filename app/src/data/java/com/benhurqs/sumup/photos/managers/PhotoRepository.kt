package com.benhurqs.sumup.photos.managers

import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.clients.local.PhotoLocalDataSource
import com.benhurqs.sumup.main.domains.entities.Photo
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


open class PhotoRepository(
    val remoteDataSource: PhotoDataSource,
    val localDataSource: PhotoLocalDataSource,
    val ioScheduler: Scheduler = Schedulers.io(),
    val mainScheduler: Scheduler = AndroidSchedulers.mainThread()
) {

    var cachedPhotoList = HashMap<Int, List<Photo>>()

    companion object {
        private var mInstance: PhotoRepository? = null

        @Synchronized
        fun getInstance(
            remoteDataSource: PhotoDataSource,
            localDataSource: PhotoLocalDataSource
        ): PhotoRepository {
            if (mInstance == null) {
                mInstance = PhotoRepository(remoteDataSource, localDataSource)
            }
            return mInstance!!
        }
    }


    fun getPhotoList(albumID: Int, callback: APICallback<List<Photo>?>) {
        var cachedList = cachedPhotoList.get(albumID)
        if (cachedList.isNullOrEmpty()) {
            callLocalDatabase(albumID, callback)
        } else {
            callback.onStart()
            callback.onSuccess(cachedList)
            callback.onFinish()

            callRemoteAPI(albumID)
        }

    }

    private fun callLocalDatabase(albumID: Int, callback: APICallback<List<Photo>?>) {
        localDataSource.getPhotoList(albumID)
            .observeOn(mainScheduler)
            .subscribeOn(ioScheduler)
            .doOnSubscribe {
                callback.onStart()
            }
            .subscribe(object : Observer<List<Photo>?> {
                override fun onError(e: Throwable?) {
                    callRemoteAPI(albumID, callback)
                }

                override fun onNext(list: List<Photo>?) {
                    managerLocalResult(albumID, list, callback)
                }

                override fun onComplete() {}
                override fun onSubscribe(d: Disposable?) {}

            })
    }

    fun managerLocalResult(albumID: Int, list: List<Photo>?, callback: APICallback<List<Photo>?>) {
        if (list.isNullOrEmpty()) {
            callRemoteAPI(albumID, callback)
        } else {
            callback.onSuccess(list)
            callback.onFinish()

            //update local data
            callRemoteAPI(albumID)
        }
    }


    private fun callRemoteAPI(albumID: Int, callback: APICallback<List<Photo>?>? = null) {
        remoteDataSource.getPhotoList(albumID)
            .observeOn(mainScheduler)
            .subscribeOn(ioScheduler)
            .doOnSubscribe {
                callback?.onStart()
            }
            .subscribe(object : Observer<List<Photo>?> {
                override fun onError(e: Throwable?) {
                    callback?.onError()
                }

                override fun onNext(value: List<Photo>?) {
                    callback?.onSuccess(value)

                    if (!value.isNullOrEmpty()) {
                        savePhotos(albumID, value)
                    }
                }

                override fun onComplete() {
                    callback?.onFinish()
                }

                override fun onSubscribe(d: Disposable?) {}

            })
    }

    fun savePhotos(albumId: Int, list: List<Photo>) {
        cachedPhotoList.put(albumId, list)
        list.forEach { photo ->
            localDataSource.savePhotos(photo)
        }
    }
}