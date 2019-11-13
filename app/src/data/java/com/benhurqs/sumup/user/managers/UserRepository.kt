package com.benhurqs.sumup.user.managers

import android.util.Log
import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.splash.domains.entities.User
import com.benhurqs.sumup.user.clients.local.UserLocalDataSource
import com.benhurqs.sumup.user.clients.remote.UserRemoteDataSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserRepository( val remoteDataSource: UserDataSource, val localDataSource: UserLocalDataSource) {


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

    fun getUserList(callback: APICallback<List<User>, String>){
        remoteDataSource.getUserList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                callback.onStart()
            }
            .subscribe(object : Observer<List<User>?> {
                override fun onError(e: Throwable?) {
                    if(e?.message != null){
                        callback.onError(e.message!!)
                    }
                }

                override fun onNext(list: List<User>?) {
                    if(list.isNullOrEmpty()){
                        callback.onError("")
                    }else{
                        callback.onSuccess(list)
                        saveUser(list)
                    }
                }

                override fun onComplete() {
                    callback.onFinish()
                }

                override fun onSubscribe(d: Disposable?) {}

            })

        getUserLocal()
    }

    fun getUserLocal(){
        localDataSource.getUserList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                Log.e("Local", "comecou")
            }
            .subscribe(object : Observer<List<User>?> {
                override fun onError(e: Throwable?) {
                    if(e?.message != null){
                        Log.e("Local", "Error" + e.message)
                    }else{
                        Log.e("Local", "Error")
                    }
                }

                override fun onNext(value: List<User>?) {
                    if(value.isNullOrEmpty()){
                        Log.e("Local", "esta vazio")
                    }else{
                        Log.e("Local", "Ushhu - " + value.size)
                    }
                }

                override fun onComplete() {
                    Log.e("Local", "Finalizou")
                }

                override fun onSubscribe(d: Disposable?) {}

            })
    }

    fun saveUser(list : List<User>){
        list.forEach {user ->
            localDataSource.saveUser(user)
        }
    }

}