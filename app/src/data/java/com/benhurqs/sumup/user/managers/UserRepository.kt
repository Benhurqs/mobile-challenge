package com.benhurqs.sumup.user.managers

import android.util.Log
import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.splash.domains.entities.User
import com.benhurqs.sumup.user.clients.remote.UserRemoteDataSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserRepository {

    private val remoteDataSource: UserDataSource
//    var localDataSource: UserDataSource

    companion object {
        private var mInstance: UserRepository? = null

        @Synchronized
        fun getInstance(): UserRepository {
            if(mInstance == null){
                mInstance = UserRepository()
            }
            return mInstance!!
        }
    }

    init {
        remoteDataSource = UserRemoteDataSource.getInstance()
//        localDataSource = UserLocalDataSource.getInstance()
    }

    fun getUserList(callback: APICallback<List<User>, String>){
        remoteDataSource.getUserList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                callback.onStart()
            }
            .subscribe(object : Observer<List<User>> {
                override fun onError(e: Throwable?) {
                    if(e?.message != null){
                        callback.onError(e.message!!)
                    }
                }

                override fun onNext(value: List<User>?) {
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