package com.benhurqs.sumup.photos.managers

import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.clients.local.PhotoLocalDataSource
import com.benhurqs.sumup.photos.clients.remote.PhotoRemoteDataSource
import com.benhurqs.sumup.main.domains.entities.Photo
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PhotoRepositoryTest{
    private val albumId = 1

    @Mock
    private lateinit var remoteDataSouce: PhotoRemoteDataSource

    @Mock
    private lateinit var localDataSource: PhotoLocalDataSource
    private lateinit var repository: PhotoRepository

    @Mock
    private lateinit var callback: APICallback<List<Photo>?>

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)

        val testScheduler = TestScheduler()
        repository = PhotoRepository(remoteDataSouce, localDataSource, testScheduler, testScheduler)
    }

    @Test
    fun `check if onSuccess() is called when cache is not null`(){
        initLocalObservable()
        initRemoteObservable()

        var list = getPhotoListMock()
        repository.cachedPhotoList.put(albumId, list)
        repository.getPhotoList(albumId, callback)

        Mockito.verify(remoteDataSouce, Mockito.times(1)).getPhotoList(albumId)
        Mockito.verify(localDataSource, Mockito.never()).getPhotoList(albumId)
        Mockito.verify(callback, Mockito.times(1)).onStart()
        Mockito.verify(callback, Mockito.times(1)).onSuccess(list)
        Mockito.verify(callback, Mockito.times(1)).onFinish()
    }

    @Test
    fun `check if local database is called when cache is empty`(){
        initLocalObservable()
        initRemoteObservable()

        repository.getPhotoList(albumId, callback)

        Mockito.verify(callback, Mockito.times(1)).onStart()
        Mockito.verify(localDataSource, Mockito.times(1)).getPhotoList(albumId)
    }

    @Test
    fun `check is onSuccess is called when list is not null`(){
        initRemoteObservable()
        val list = getPhotoListMock()

        repository.managerLocalResult(albumId, list, callback)
        Mockito.verify(callback, Mockito.times(1)).onSuccess(list)
        Mockito.verify(callback, Mockito.times(1)).onFinish()
        Mockito.verify(remoteDataSouce, Mockito.times(1)).getPhotoList(albumId)
    }

    @Test
    fun `check is remote API is called when list is null`(){
        initRemoteObservable()

        repository.managerLocalResult(albumId, null, callback)
        Mockito.verify(remoteDataSouce, Mockito.times(1)).getPhotoList(albumId)
    }

    @Test
    fun `check if local database save user is called`(){
        var list = getPhotoListMock()
        var count  = list.size

        repository.savePhotos(albumId, list)
        Assert.assertTrue(repository.cachedPhotoList.get(albumId) == list)
    }

    private fun getPhotoListMock() = ArrayList<Photo>().apply {
        add(Photo())
    }

    private fun initRemoteObservable(){
        Mockito.`when`(remoteDataSouce.getPhotoList(albumId)).thenReturn(
            Observable.just(getPhotoListMock())
        )
    }

    private fun initLocalObservable(){
        Mockito.`when`(localDataSource.getPhotoList(albumId)).thenReturn(
            Observable.just(getPhotoListMock())
        )
    }

}