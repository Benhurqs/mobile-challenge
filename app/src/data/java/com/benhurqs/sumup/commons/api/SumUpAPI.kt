package com.benhurqs.sumup.commons.api

import com.benhurqs.sumup.photos.domains.entities.Album
import com.benhurqs.sumup.photos.domains.entities.Photo
import com.benhurqs.sumup.splash.domains.entities.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface SumUpAPI{

    @GET("users")
    fun users(): Observable<List<User>>

    @GET("albums")
    fun albums(@Query("userId") userID: Int): Observable<List<Album>>

    @GET("photos")
    fun photos(@Query("albumID") albumID: Int): Observable<List<Photo>>

}