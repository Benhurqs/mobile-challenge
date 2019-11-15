package com.benhurqs.sumup.user.clients.remote

import com.benhurqs.sumup.commons.api.SumUpAPIService
import com.benhurqs.sumup.photos.domains.entities.User
import com.benhurqs.sumup.user.managers.UserDataSource
import io.reactivex.Observable

open class UserRemoteDataSource : UserDataSource{

    private val clientAPI: SumUpAPIService

    companion object {
        private var mInstance: UserRemoteDataSource? = null

        @Synchronized
        fun getInstance(): UserRemoteDataSource {
            if(mInstance == null){
                mInstance = UserRemoteDataSource()
            }
            return mInstance!!
        }
    }

    init {
        clientAPI = SumUpAPIService()
    }

    override fun getUserList(): Observable<List<User>?> = clientAPI.getUsers()
}