package com.benhurqs.sumup.photos.presentation.presenter

import com.benhurqs.sumup.album.managers.AlbumRepository
import com.benhurqs.sumup.main.domains.entities.Album
import com.benhurqs.sumup.main.presentation.contracts.AlbumContract
import com.benhurqs.sumup.main.presentation.contracts.MainView
import com.benhurqs.sumup.main.presentation.presenter.AlbumPresenter
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class AlbumPresenterTest{

    private lateinit var presenter: AlbumPresenter
    private lateinit var albumView: AlbumContract.View
    private lateinit var mainView: MainView
    private lateinit var repository: AlbumRepository

    @Before
    fun setUp(){
        albumView = mock(AlbumContract.View::class.java)
        mainView = mock(MainView::class.java)
        repository = mock(AlbumRepository::class.java)
        presenter = AlbumPresenter(
            albumView,
            mainView,
            repository
        )
    }

    @Test
    fun `check if error is called when userID is null`(){
        `when`(mainView.isAdded()).thenReturn(true)
        presenter.callAlbumAPI(null)
        verify(mainView, times(1)).isAdded()
        verify(mainView, times(1)).showError()
    }

    @Test
    fun `check if show loading when onStart() is called`(){
        `when`(mainView.isAdded()).thenReturn(true)
        presenter.onStart()

        verify(mainView, times(1)).isAdded()
        verify(mainView, times(1)).showLoading()
        verify(mainView, times(1)).hideError()
        verify(albumView, times(1)).hideEmptyView()
    }

    @Test
    fun `check if do nothing when onStart() is called and isAdded is false`(){
        `when`(mainView.isAdded()).thenReturn(false)
        presenter.onStart()

        verify(mainView, times(1)).isAdded()
        verify(mainView, never()).showLoading()
        verify(albumView, never()).hideEmptyView()
    }

    @Test
    fun `check if do nothing when onFinish() is called and isAdded is false`(){
        `when`(mainView.isAdded()).thenReturn(false)
        presenter.onFinish()

        verify(mainView, times(1)).isAdded()
        verify(mainView, never()).hideLoading()
    }

    @Test
    fun `check if hide loading when onFinish() is called`(){
        `when`(mainView.isAdded()).thenReturn(true)
        presenter.onFinish()

        verify(mainView, times(1)).isAdded()
        verify(mainView, times(1)).hideLoading()
    }

    @Test
    fun `check if do nothing when onSuccess() is called and isAdded is false`(){
        `when`(mainView.isAdded()).thenReturn(false)
        var albumList = ArrayList<Album>()

        presenter.onSuccess(albumList)

        verify(mainView, times(1)).isAdded()
        verify(albumView, never()).loadingAlbums(albumList)
    }

    @Test
    fun `check if load list when onSuccess() is called`(){
        `when`(mainView.isAdded()).thenReturn(true)
        var albumList = ArrayList<Album>()
        albumList.add(Album().apply {
            id = 1
            title = "title"
            userId = 2
        })

        presenter.onSuccess(albumList)

        verify(mainView, times(1)).isAdded()
        verify(albumView, never()).showEmptyView()
        verify(albumView, times(1)).loadingAlbums(albumList)
    }

    @Test
    fun `check if show empty view when onSuccess() is called and list is empty`(){
        `when`(mainView.isAdded()).thenReturn(true)
        var albumList = ArrayList<Album>()

        presenter.onSuccess(albumList)

        verify(mainView, times(1)).isAdded()
        verify(albumView, times(1)).showEmptyView()
        verify(albumView, never()).loadingAlbums(albumList)
    }

    @Test
    fun `check if show empty view when onSuccess() is called and list is null`(){
        `when`(mainView.isAdded()).thenReturn(true)

        presenter.onSuccess(null)

        verify(mainView, times(1)).isAdded()
        verify(albumView, times(1)).showEmptyView()
    }

    @Test
    fun `check if show error when onFinish() is called`(){
        `when`(mainView.isAdded()).thenReturn(true)

        presenter.onError()

        verify(mainView, times(1)).isAdded()
        verify(mainView, times(1)).showError()
    }

    @Test
    fun `check if do nothing when onError() is called and isAdded is false`(){
        `when`(mainView.isAdded()).thenReturn(false)

        presenter.onError()

        verify(mainView, times(1)).isAdded()
        verify(mainView, never()).showError()
    }

}