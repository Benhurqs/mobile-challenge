package com.benhurqs.sumup.user.managers

import android.util.Log
import com.benhurqs.sumup.commons.api.SumUpAPIService
import com.benhurqs.sumup.splash.domains.entities.User
import com.benhurqs.sumup.user.clients.remote.UserRemoteDataSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class UserRepository {

//    var remoteDataSouce: UserDataSource
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
//        remoteDataSouce = UserRemoteDataSource.getInstance()
//        localDataSource = UserLocalDataSource.getInstance()
    }

    fun getUserList(){
        SumUpAPIService().getUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<List<User>> {
                override fun onError(e: Throwable?) {
                    Log.e("error", e?.message)
                }

                override fun onNext(value: List<User>?) {
                    Log.e("chegou",  "aqui" + value!![0].name)
                }

                override fun onComplete() {
                    Log.e("Acabou", "Acabou")
                }

                override fun onSubscribe(d: Disposable?) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
    }

}