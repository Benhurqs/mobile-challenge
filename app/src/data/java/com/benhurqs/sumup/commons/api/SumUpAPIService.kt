package com.benhurqs.sumup.commons.api

import com.benhurqs.sumup.BuildConfig
import com.benhurqs.sumup.photos.domains.entities.Album
import com.benhurqs.sumup.photos.domains.entities.Photo
import com.benhurqs.sumup.splash.domains.entities.User
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SumUpAPIService  {

    private val retrofit: Retrofit
    private val api: SumUpAPI
    private val TIMEOUT: Long = 20

    init {

        val httpClient = OkHttpClient.Builder().apply {

            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG){
                val logging = HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)

                addInterceptor(logging).build()
            }
        }.build()





        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()

        api = retrofit.create(SumUpAPI::class.java)
    }

    fun getUsers(): Observable<List<User>> = api.users()
    fun getAlbums(userID: Int): Observable<List<Album>> = api.albums(userID)
    fun getPhotos(albumID: Int): Observable<List<Photo>> = api.photos(albumID)


}