package com.benhurqs.sumup.photos.presentation.ui.presenter

import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.photos.domains.entities.User
import com.benhurqs.sumup.photos.presentation.ui.contracts.MainView
import com.benhurqs.sumup.photos.presentation.ui.contracts.UserContract
import com.benhurqs.sumup.user.managers.UserRepository

class UserPresenter (private val userView: UserContract.View, private val mainView: MainView,  private val userRepository: UserRepository): UserContract.Presenter, APICallback<List<User>, String>{

    override fun callUserAPI() {
        userRepository.getUserList(this)
    }

    override fun onStart() {
        if(mainView.isAdded()) {
            userView.showUserLoading()
        }
    }

    override fun onError(error: String) {
        if(mainView.isAdded()) {
            userView.showError(error)
            userView.hideUserLoading()
            userView.hideUserContent()
        }
    }

    override fun onFinish() {
        if(mainView.isAdded()) {
            userView.hideUserLoading()
            mainView.hideLoading()
        }
    }

    override fun onSuccess(response: List<User>) {
        if(mainView.isAdded()){
            userView.loadingUsers(response)
            userView.showUserContent()
        }
    }

    override fun selectedUser(user: User) {
        if(mainView.isAdded()){
            userView.loadginHeader(user)
            userView.hideUserContent()
        }
    }
}