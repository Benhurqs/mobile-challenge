package com.benhurqs.sumup.user.managers

import com.benhurqs.sumup.main.domains.entities.User
import io.reactivex.Observable


interface UserDataSource {
    fun getUserList() : Observable<List<User>?>
}