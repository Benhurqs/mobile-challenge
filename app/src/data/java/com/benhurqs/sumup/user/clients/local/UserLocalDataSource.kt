package com.benhurqs.sumup.user.clients.local

import android.content.Context
import com.benhurqs.sumup.commons.api.SumUpAPIService
import com.benhurqs.sumup.splash.domains.entities.User
import com.benhurqs.sumup.user.clients.remote.UserRemoteDataSource
import com.benhurqs.sumup.user.managers.UserDataSource
import io.reactivex.Observable

class UserLocalDataSource(context: Context) : UserDataSource{

    private val userDAO: UserDao

    companion object {
        private var mInstance: UserLocalDataSource? = null

        @Synchronized
        fun getInstance(context: Context): UserLocalDataSource {
            if(mInstance == null){
                mInstance = UserLocalDataSource(context)
            }
            return mInstance!!
        }
    }

    init {
        userDAO = UserRoomDatabase.getDatabase(context).userDao()
    }


    override fun getUserList(): Observable<List<User>?> {
        return userDAO.getUsers()
    }

    fun saveUser(user: User){
        UserRoomDatabase.databaseWriteExecutor.execute {
            userDAO.insert(user)
        }
    }
}