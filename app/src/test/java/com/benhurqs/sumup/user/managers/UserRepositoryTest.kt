package com.benhurqs.sumup.user.managers

import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.User
import com.benhurqs.sumup.user.clients.local.UserLocalDataSource
import com.benhurqs.sumup.user.clients.remote.UserRemoteDataSource
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class UserRepositoryTest{

    @Mock
    private lateinit var remoteDataSouce: UserRemoteDataSource

    @Mock
    private lateinit var localDataSource: UserLocalDataSource
    private lateinit var repository: UserRepository

    @Mock
    private lateinit var callback: APICallback<List<User>?>

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)

        val testScheduler = TestScheduler()
        repository = UserRepository(remoteDataSouce, localDataSource, testScheduler, testScheduler)

    }

    @Test
    fun `check if call onSuccess if cache is not null or empty`(){
        initRemoteObservable()

        var list = getUserListMock()
        repository.cachedUserList = list

        repository.getUserList(callback)

        Mockito.verify(localDataSource, Mockito.never()).getUserList()
        Mockito.verify(remoteDataSouce, Mockito.times(1)).getUserList()
        Mockito.verify(callback, Mockito.times(1)).onStart()
        Mockito.verify(callback, Mockito.times(1)).onFinish()
        Mockito.verify(callback, Mockito.times(1)).onSuccess(list)
    }

    @Test
    fun `check if call local database if cache is null`(){
        initLocalObservable()
        initRemoteObservable()

        repository.cachedUserList = null
        repository.getUserList(callback)
        Mockito.verify(localDataSource, Mockito.times(1)).getUserList()
    }


    @Test
    fun `check if call local database if cache is empty`(){
        initLocalObservable()
        initRemoteObservable()

        repository.cachedUserList = ArrayList<User>()
        repository.getUserList(callback)
        Mockito.verify(localDataSource, Mockito.times(1)).getUserList()
        Mockito.verify(callback, Mockito.times(1)).onStart()

    }

    @Test
    fun `check if onSuccess() is called when local database is not null`(){
        var list = getUserListMock()
        Mockito.`when`(localDataSource.getUserList()).thenReturn(
            Observable.just(list)
        )

        repository.cachedUserList = null

        repository.getUserList(callback)
        Mockito.verify(localDataSource, Mockito.times(1)).getUserList()
        Mockito.verify(callback, Mockito.times(1)).onStart()
    }

    @Test
    fun `check if onSuccess() is called when list is not null`(){
        initRemoteObservable()
        var list = getUserListMock()

        repository.managerLocalResult(list, callback)
        Mockito.verify(callback, Mockito.times(1)).onFinish()
        Mockito.verify(callback, Mockito.times(1)).onSuccess(list)
    }

    @Test
    fun `check if remote API is called when list is null`(){
        initRemoteObservable()

        repository.managerLocalResult(null, callback)
        Mockito.verify(remoteDataSouce, Mockito.times(1)).getUserList()
        Mockito.verify(callback, Mockito.times(1)).onStart()
    }

    @Test
    fun `check if local database save user is called`(){
        var list = getUserListMock()
        var count  = list.size

        repository.saveUser(list)
        Assert.assertTrue(repository.cachedUserList == list)
    }


    private fun getUserListMock() = ArrayList<User>().apply {
        add(User())
    }

    private fun initRemoteObservable(){
        Mockito.`when`(remoteDataSouce.getUserList()).thenReturn(
            Observable.just(getUserListMock())
        )
    }

    private fun initLocalObservable(){
        Mockito.`when`(localDataSource.getUserList()).thenReturn(
            Observable.just(getUserListMock())
        )
    }


}