package com.benhurqs.sumup.injection

import android.content.Context
import com.benhurqs.sumup.album.clients.local.AlbumLocalDataSource
import com.benhurqs.sumup.album.clients.remote.AlbumRemoteDataSource
import com.benhurqs.sumup.album.managers.AlbumRepository
import com.benhurqs.sumup.photos.clients.local.PhotoLocalDataSource
import com.benhurqs.sumup.photos.clients.remote.PhotoRemoteDataSource
import com.benhurqs.sumup.photos.managers.PhotoRepository
import com.benhurqs.sumup.user.clients.local.UserLocalDataSource
import com.benhurqs.sumup.user.clients.remote.UserRemoteDataSource
import com.benhurqs.sumup.user.managers.UserRepository

object Injection {

    fun provideAlbumRepository(context: Context): AlbumRepository {
        return AlbumRepository.getInstance(
            AlbumRemoteDataSource.getInstance(),
            AlbumLocalDataSource.getInstance(context)
        )
    }

    fun providePhotoRepository(context: Context): PhotoRepository {
        return PhotoRepository.getInstance(
            PhotoRemoteDataSource.getInstance(),
            PhotoLocalDataSource.getInstance(context)
        )
    }

    fun provideUserRepository(context: Context): UserRepository {
        return UserRepository.getInstance(
            UserRemoteDataSource.getInstance(),
            UserLocalDataSource.getInstance(context)
        )
    }
}