package com.benhurqs.sumup.photos.presentation.ui.contracts

import com.benhurqs.sumup.photos.domains.entities.User

interface UserContract {
    interface View{
        fun showUserLoading()
        fun hideUserLoading()
        fun showError(error: String)
        fun hideUserContent()
        fun showUserContent()
        fun loadingUsers(userList: List<User>)
        fun loadginHeader(user: User)
    }

    interface Presenter{
        fun callUserAPI()
        fun selectedUser(user: User)

    }
}