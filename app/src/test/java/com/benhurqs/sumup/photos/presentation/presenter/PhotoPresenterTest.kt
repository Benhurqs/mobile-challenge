package com.benhurqs.sumup.photos.presentation.presenter

import com.benhurqs.sumup.photos.domains.entities.Photo
import com.benhurqs.sumup.photos.managers.PhotoRepository
import com.benhurqs.sumup.photos.presentation.contracts.PhotoContract
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class PhotoPresenterTest{

    private lateinit var presenter: PhotoPresenter
    private lateinit var view: PhotoContract.View
    private lateinit var repository: PhotoRepository

    @Before
    fun setUp(){
        view = Mockito.mock(PhotoContract.View::class.java)
        repository = Mockito.mock(PhotoRepository::class.java)
        presenter = PhotoPresenter(view, repository)
    }

    @Test
    fun `check if error is called when albumID is null`(){
        Mockito.`when`(view.isAdded()).thenReturn(true)
        presenter.callPhotoAPI(null)
        Mockito.verify(view, Mockito.times(1)).isAdded()
        Mockito.verify(view, Mockito.times(1)).showError()
    }

    @Test
    fun `check if show loading when onStart() is called`(){
        Mockito.`when`(view.isAdded()).thenReturn(true)
        presenter.onStart()

        Mockito.verify(view, Mockito.times(1)).isAdded()
        Mockito.verify(view, Mockito.times(1)).showLoading()
        Mockito.verify(view, Mockito.times(1)).hideEmptyView()
        Mockito.verify(view, Mockito.times(1)).hideError()
    }

    @Test
    fun `check if do nothing when onStart() is called and isAdded is false`(){
        Mockito.`when`(view.isAdded()).thenReturn(false)
        presenter.onStart()

        Mockito.verify(view, Mockito.times(1)).isAdded()
        Mockito.verify(view, Mockito.never()).showLoading()
        Mockito.verify(view, Mockito.never()).hideEmptyView()
    }


    @Test
    fun `check if show error when onError() is called`(){
        Mockito.`when`(view.isAdded()).thenReturn(true)
        presenter.onError()

        Mockito.verify(view, Mockito.times(1)).isAdded()
        Mockito.verify(view, Mockito.times(1)).showError()
    }

    @Test
    fun `check if do nothing when onError() is called and isAdded is false`(){
        Mockito.`when`(view.isAdded()).thenReturn(false)
        presenter.onError()

        Mockito.verify(view, Mockito.times(1)).isAdded()
        Mockito.verify(view, Mockito.never()).showError()
    }

    @Test
    fun `check if hide loading when onFinish() is called`(){
        Mockito.`when`(view.isAdded()).thenReturn(true)
        presenter.onFinish()

        Mockito.verify(view, Mockito.times(1)).isAdded()
        Mockito.verify(view, Mockito.times(1)).hideLoading()
    }

    @Test
    fun `check if do nothing when onFinish() is called and isAdded is false`(){
        Mockito.`when`(view.isAdded()).thenReturn(false)
        presenter.onFinish()

        Mockito.verify(view, Mockito.times(1)).isAdded()
        Mockito.verify(view, Mockito.never()).hideLoading()
    }

    @Test
    fun `check if do nothing when onSuccess() is called and isAdded is false`(){
        Mockito.`when`(view.isAdded()).thenReturn(false)
        var photoList = ArrayList<Photo>()

        presenter.onSuccess(photoList)

        Mockito.verify(view, Mockito.times(1)).isAdded()
        Mockito.verify(view, Mockito.never()).loadPhotos(photoList)
    }

    @Test
    fun `check if load list when onSuccess() is called`(){
        Mockito.`when`(view.isAdded()).thenReturn(true)
        var photoList = ArrayList<Photo>()
        photoList.add(Photo().apply {
            id = 1
            url = "image"
            albumID = 2
        })

        presenter.onSuccess(photoList)

        Mockito.verify(view, Mockito.times(1)).isAdded()
        Mockito.verify(view, Mockito.never()).showEmptyView()
        Mockito.verify(view, Mockito.times(1)).loadPhotos(photoList)
    }

    @Test
    fun `check if show empty view when onSuccess() is called and list is empty`(){
        Mockito.`when`(view.isAdded()).thenReturn(true)
        var photoList = ArrayList<Photo>()

        presenter.onSuccess(photoList)

        Mockito.verify(view, Mockito.times(1)).isAdded()
        Mockito.verify(view, Mockito.times(1)).showEmptyView()
        Mockito.verify(view, Mockito.never()).loadPhotos(photoList)
    }

    @Test
    fun `check if show empty view when onSuccess() is called and list is null`(){
        Mockito.`when`(view.isAdded()).thenReturn(true)
        presenter.onSuccess(null)

        Mockito.verify(view, Mockito.times(1)).isAdded()
        Mockito.verify(view, Mockito.times(1)).showEmptyView()
    }
}