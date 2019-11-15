package com.benhurqs.sumup.photos.presentation.presenter

import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.User
import com.benhurqs.sumup.photos.presentation.contracts.MainView
import com.benhurqs.sumup.photos.presentation.contracts.UserContract
import com.benhurqs.sumup.user.managers.UserRepository

class UserPresenter(
    private val userView: UserContract.View,
    private val mainView: MainView,
    private val userRepository: UserRepository
) : UserContract.Presenter, APICallback<List<User>?> {

    var hasUser = false

    override fun callUserAPI() {
        userRepository.getUserList(this)
    }

    override fun onStart() {
        if (mainView.isAdded()) {
            userView.showUserLoading()
            userView.hideEmptyUserView()
            userView.hideUserError()

            if(hasUser){
                userView.showCloseButton()
            }else{
                userView.hideCloseButton()
            }
        }
    }

    override fun onError() {
        if (mainView.isAdded()) {
            userView.showUserError()
            userView.hideUserLoading()
            userView.hideUserContent()
        }
    }

    override fun onFinish() {
        if (mainView.isAdded()) {
            userView.hideUserLoading()
            mainView.hideLoading()
        }
    }

    override fun onSuccess(userList: List<User>?) {
        if (mainView.isAdded()) {
            if (userList.isNullOrEmpty()) {
                userView.showEmptyUserView()
            } else {
                userView.loadingUsers(userList)
                userView.showUserContent()
            }

        }
    }

    override fun selectedUser(user: User?) {
        if (user == null) {
            hasUser = false
            onError()
        } else {
            if (mainView.isAdded()) {
                hasUser = true
                userView.loadingHeader(user)
                userView.hideUserContent()
            }

        }
    }
}