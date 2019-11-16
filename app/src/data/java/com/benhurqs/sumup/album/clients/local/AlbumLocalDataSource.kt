package com.benhurqs.sumup.album.clients.local

import android.content.Context
import com.benhurqs.sumup.album.managers.AlbumDataSource
import com.benhurqs.sumup.photos.domains.entities.Album
import io.reactivex.Observable

open class AlbumLocalDataSource(context: Context): AlbumDataSource {

    private val albumDao: AlbumDao

    companion object {
        private var mInstance: AlbumLocalDataSource? = null

        @Synchronized
        fun getInstance(context: Context): AlbumLocalDataSource {
            if(mInstance == null){
                mInstance = AlbumLocalDataSource(context)
            }
            return mInstance!!
        }
    }

    init {
        albumDao = AlbumRoomDatabase.getDatabase(context).albumDao()
    }


    override fun getAlbumList(userID: Int): Observable<List<Album>?> {
        return albumDao.getAlbumsByUserId(userID)
    }

    fun saveAlbums(album: Album){
        AlbumRoomDatabase.databaseWriteExecutor.execute {
            albumDao.insert(album)
        }
    }
}