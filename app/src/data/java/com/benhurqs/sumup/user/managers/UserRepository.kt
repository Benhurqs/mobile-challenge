package com.benhurqs.sumup.user.managers

import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.User
import com.benhurqs.sumup.user.clients.local.UserLocalDataSource
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

open class UserRepository( val remoteDataSource: UserDataSource, val localDataSource: UserLocalDataSource) {

    private var cachedUserList: List<User>? = null

    companion object {
        private var mInstance: UserRepository? = null

        @Synchronized
        fun getInstance(remoteDataSource: UserDataSource, localDataSource: UserLocalDataSource): UserRepository {
            if(mInstance == null){
                mInstance = UserRepository(remoteDataSource, localDataSource)
            }
            return mInstance!!
        }
    }

    fun getUserList(callback: APICallback<List<User>?>){

        if(cachedUserList.isNullOrEmpty()){
            callLocalDatabase(callback)
        }else{
            callback.onStart()
            callback.onSuccess(cachedUserList)
            callback.onFinish()

            callRemoteAPI()
        }

    }

    private fun callRemoteAPI(callback: APICallback<List<User>?>? = null){
        remoteDataSource.getUserList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                callback?.onStart()
            }
            .subscribe(object : Observer<List<User>?> {
                override fun onError(e: Throwable?) {
                    callback?.onError()
                }

                override fun onNext(list: List<User>?) {
                    callback?.onSuccess(list)
                    if(!list.isNullOrEmpty()){
                        saveUser(list)
                    }
                }

                override fun onComplete() {
                    callback?.onFinish()
                }

                override fun onSubscribe(d: Disposable?) {}

            })


    }
    private fun callLocalDatabase(callback: APICallback<List<User>?>){
        localDataSource.getUserList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                callback.onStart()
            }
            .subscribe(object : Observer<List<User>?> {
                override fun onError(e: Throwable?) {
                    callRemoteAPI(callback)
                }

                override fun onNext(list: List<User>?) {
                    if(list.isNullOrEmpty()){
                        callRemoteAPI(callback)
                    }else{
                        callback.onSuccess(list)
                        callback.onFinish()

                        //update local database
                        callRemoteAPI(null)
                    }

                }

                override fun onComplete() {}

                override fun onSubscribe(d: Disposable?) {}

            })

    }

    fun saveUser(list : List<User>){
        cachedUserList = list
        list.forEach {user ->
            localDataSource.saveUser(user)
        }
    }

}