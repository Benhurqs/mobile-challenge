package com.benhurqs.sumup.album.managers

import com.benhurqs.sumup.album.clients.local.AlbumLocalDataSource
import com.benhurqs.sumup.album.clients.remote.AlbumRemoteDataSource
import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.Album
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AlbumRepositoryTest{
    private val userId = 1

    @Mock
    private lateinit var remoteDataSouce: AlbumRemoteDataSource

    @Mock
    private lateinit var localDataSource: AlbumLocalDataSource
    private lateinit var repository: AlbumRepository

    @Mock
    private lateinit var callback: APICallback<List<Album>?>

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)

        val testScheduler = TestScheduler()
        repository = AlbumRepository(remoteDataSouce, localDataSource, testScheduler, testScheduler)
    }

    @Test
    fun `check if onSuccess() is called when cache is not null`(){
        initLocalObservable()
        initRemoteObservable()

        var list = getAlbumListMock()
        repository.cachedAlbumList.put(userId, list)
        repository.getAlbumList(userId, callback)

        Mockito.verify(remoteDataSouce, Mockito.times(1)).getAlbumList(userId)
        Mockito.verify(localDataSource, Mockito.never()).getAlbumList(userId)
        Mockito.verify(callback, Mockito.times(1)).onStart()
        Mockito.verify(callback, Mockito.times(1)).onSuccess(list)
        Mockito.verify(callback, Mockito.times(1)).onFinish()
    }

    @Test
    fun `check if local database is called when cache is empty`(){
        initLocalObservable()
        initRemoteObservable()

        repository.getAlbumList(userId, callback)

        Mockito.verify(callback, Mockito.times(1)).onStart()
        Mockito.verify(localDataSource, Mockito.times(1)).getAlbumList(userId)
    }

    @Test
    fun `check is onSuccess is called when list is not null`(){
        initRemoteObservable()
        val list = getAlbumListMock()

        repository.managerLocalResult(userId, list, callback)
        Mockito.verify(callback, Mockito.times(1)).onSuccess(list)
        Mockito.verify(callback, Mockito.times(1)).onFinish()
        Mockito.verify(remoteDataSouce, Mockito.times(1)).getAlbumList(userId)
    }

    @Test
    fun `check is remote API is called when list is null`(){
        initRemoteObservable()

        repository.managerLocalResult(userId, null, callback)
        Mockito.verify(remoteDataSouce, Mockito.times(1)).getAlbumList(userId)
    }

    @Test
    fun `check if local database save user is called`(){
        var list = getAlbumListMock()
        var count  = list.size

        repository.saveAlbums(userId, list)
        Assert.assertTrue(repository.cachedAlbumList.get(userId) == list)
    }

    private fun getAlbumListMock() = ArrayList<Album>().apply {
        add(Album())
    }

    private fun initRemoteObservable(){
        Mockito.`when`(remoteDataSouce.getAlbumList(userId)).thenReturn(
            Observable.just(getAlbumListMock())
        )
    }

    private fun initLocalObservable(){
        Mockito.`when`(localDataSource.getAlbumList(userId)).thenReturn(
            Observable.just(getAlbumListMock())
        )
    }
}