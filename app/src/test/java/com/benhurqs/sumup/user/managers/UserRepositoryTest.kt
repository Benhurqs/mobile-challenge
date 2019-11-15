package com.benhurqs.sumup.user.managers

import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.User
import com.benhurqs.sumup.user.clients.local.UserLocalDataSource
import com.benhurqs.sumup.user.clients.remote.UserRemoteDataSource
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class UserRepositoryTest{

    private lateinit var remoteDataSouce: UserRemoteDataSource
    private lateinit var localDataSource: UserLocalDataSource
    private lateinit var repository: UserRepository

    @Before
    fun setUp(){
        remoteDataSouce = Mockito.mock(UserRemoteDataSource::class.java)
        localDataSource = Mockito.mock(UserLocalDataSource::class.java)
        repository = UserRepository(remoteDataSouce, localDataSource)
    }

    @Test
    fun teste(){
        var user = User().apply {
            id = 1
            image = "image"
            name = "nome"
        }

        var list = ArrayList<User>().apply {
            add(user)
        }

        Mockito.`when`(remoteDataSouce.getUserList()).thenReturn(
            Observable.just(list)
        )
        repository.getUserList(object : APICallback<List<User>?>{
            override fun onStart() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFinish() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSuccess(response: List<User>?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


        Mockito.verify(remoteDataSouce, Mockito.times(1)).getUserList()
    }
}