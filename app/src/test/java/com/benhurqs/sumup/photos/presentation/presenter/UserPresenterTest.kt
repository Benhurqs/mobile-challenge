package com.benhurqs.sumup.photos.presentation.presenter

import com.benhurqs.sumup.main.domains.entities.User
import com.benhurqs.sumup.main.presentation.contracts.MainView
import com.benhurqs.sumup.main.presentation.contracts.UserContract
import com.benhurqs.sumup.main.presentation.presenter.UserPresenter
import com.benhurqs.sumup.user.managers.UserRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class UserPresenterTest{
    private lateinit var presenter: UserPresenter
    private lateinit var userView: UserContract.View
    private lateinit var mainView: MainView
    private lateinit var repository: UserRepository

    @Before
    fun setUp(){
        userView = Mockito.mock(UserContract.View::class.java)
        mainView = Mockito.mock(MainView::class.java)
        repository = Mockito.mock(UserRepository::class.java)
        presenter = UserPresenter(
            userView,
            mainView,
            repository
        )
    }

    @Test
    fun `check if show loading when onStart() is called`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(true)
        presenter.onStart()

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(userView, Mockito.times(1)).showUserLoading()
        Mockito.verify(userView, Mockito.times(1)).hideEmptyUserView()
    }

    @Test
    fun `check if do nothing when onStart() is called and isAdded is false`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(false)
        presenter.onStart()

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(userView, Mockito.never()).showUserLoading()
        Mockito.verify(userView, Mockito.never()).hideEmptyUserView()
    }

    @Test
    fun `check if do nothing when onFinish() is called and isAdded is false`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(false)
        presenter.onFinish()

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(mainView, Mockito.never()).hideLoading()
    }

    @Test
    fun `check if hide loading when onFinish() is called`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(true)
        presenter.onFinish()

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(mainView, Mockito.times(1)).hideLoading()
    }

    @Test
    fun `check if do nothing when onSuccess() is called and isAdded is false`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(false)
        var userList = ArrayList<User>()

        presenter.onSuccess(userList)

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(userView, Mockito.never()).loadingUsers(userList)
        Mockito.verify(userView, Mockito.never()).showUserContent()
        Mockito.verify(userView, Mockito.never()).showEmptyUserView()
    }

    @Test
    fun `check if load list when onSuccess() is called`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(true)
        var list = ArrayList<User>()
        list.add(User().apply {
            id = 1
            name = "Name"
            image = "image"
        })

        presenter.onSuccess(list)

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(userView, Mockito.never()).showEmptyUserView()
        Mockito.verify(userView, Mockito.times(1)).loadingUsers(list)
        Mockito.verify(userView, Mockito.times(1)).showUserContent()
    }

    @Test
    fun `check if show empty view when onSuccess() is called and list is empty`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(true)
        var list = ArrayList<User>()

        presenter.onSuccess(list)

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(userView, Mockito.times(1)).showEmptyUserView()
        Mockito.verify(userView, Mockito.never()).loadingUsers(list)
        Mockito.verify(userView, Mockito.never()).showUserContent()
    }

    @Test
    fun `check if show empty view when onSuccess() is called and list is null`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(true)

        presenter.onSuccess(null)

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(userView, Mockito.times(1)).showEmptyUserView()
        Mockito.verify(userView, Mockito.never()).showUserContent()
    }

    @Test
    fun `check if show error when onFinish() is called`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(true)

        presenter.onError()

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(userView, Mockito.times(1)).showUserError()
        Mockito.verify(userView, Mockito.times(1)).hideUserContent()
        Mockito.verify(userView, Mockito.times(1)).hideUserLoading()
    }

    @Test
    fun `check if do nothing when onError() is called and isAdded is false`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(false)

        presenter.onError()

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(mainView, Mockito.never()).showError()
        Mockito.verify(userView, Mockito.never()).hideUserContent()
        Mockito.verify(userView, Mockito.never()).hideUserLoading()
    }

    @Test
    fun `check if do nothing when selectedUser() is called and isAdded is false`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(false)

        presenter.selectedUser(User())

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(userView, Mockito.never()).loadingHeader(User())
        Mockito.verify(userView, Mockito.never()).hideUserContent()
    }


    @Test
    fun `check if show error when selectedUser() is called and user is null`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(true)

        presenter.selectedUser(null)

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(userView, Mockito.never()).loadingHeader(User())
        Mockito.verify(userView, Mockito.times(1)).showUserError()
        Mockito.verify(userView, Mockito.times(1)).hideUserContent()
        Mockito.verify(userView, Mockito.times(1)).hideUserLoading()
    }


    @Test
    fun `check if hide content when selectedUser() is called`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(true)

        val user = User()

        presenter.selectedUser(user)

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(userView, Mockito.times(1)).loadingHeader(user)
        Mockito.verify(userView, Mockito.times(1)).hideUserContent()
        Mockito.verify(mainView, Mockito.never()).showError()
        Mockito.verify(userView, Mockito.never()).hideUserLoading()
    }

    @Test
    fun `check if show close button when onStart() is called`(){
        Mockito.`when`(mainView.isAdded()).thenReturn(true)
        presenter.hasUser = true
        presenter.onStart()

        Mockito.verify(mainView, Mockito.times(1)).isAdded()
        Mockito.verify(userView, Mockito.times(1)).showCloseButton()
        Mockito.verify(userView, Mockito.never()).hideCloseButton()
    }

}
