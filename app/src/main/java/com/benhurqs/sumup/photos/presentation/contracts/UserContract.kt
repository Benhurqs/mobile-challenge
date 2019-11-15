package com.benhurqs.sumup.photos.presentation.contracts

import com.benhurqs.sumup.photos.domains.entities.User

interface UserContract {
    interface View{
        fun showUserLoading()
        fun hideUserLoading()
        fun hideUserContent()
        fun showUserContent()
        fun loadingUsers(userList: List<User>)
        fun loadingHeader(user: User)
        fun showEmptyUserView()
        fun hideEmptyUserView()
        fun showUserError()
        fun hideUserError()
        fun showCloseButton()
        fun hideCloseButton()
    }

    interface Presenter{
        fun callUserAPI()
        fun selectedUser(user: User?)

    }
}