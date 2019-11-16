package com.benhurqs.sumup.user.managers

import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.main.domains.entities.User
import com.benhurqs.sumup.user.clients.local.UserLocalDataSource
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

open class UserRepository(
    val remoteDataSource: UserDataSource,
    val localDataSource: UserLocalDataSource,
    val ioScheduler: Scheduler = Schedulers.io(),
    val mainScheduler: Scheduler = AndroidSchedulers.mainThread()) {

    var cachedUserList: List<User>? = null

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
            .observeOn(mainScheduler)
            .subscribeOn(ioScheduler)
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
            .observeOn(mainScheduler)
            .subscribeOn(ioScheduler)
            .doOnSubscribe {
                callback.onStart()
            }
            .subscribe(object : Observer<List<User>?> {
                override fun onError(e: Throwable?) {
                    callRemoteAPI(callback)
                }

                override fun onNext(list: List<User>?) {
                    managerLocalResult(list, callback)

                }

                override fun onComplete() {}

                override fun onSubscribe(d: Disposable?) {}

            })
    }

    fun managerLocalResult(list: List<User>?, callback: APICallback<List<User>?>){
        if(list.isNullOrEmpty()){
            callRemoteAPI(callback)
        }else{
            callback.onSuccess(list)
            callback.onFinish()

            //update local database
            callRemoteAPI(null)
        }
    }

    fun saveUser(list : List<User>){
        cachedUserList = list
        list.forEach {user ->
            localDataSource.saveUser(user)
        }
    }

}