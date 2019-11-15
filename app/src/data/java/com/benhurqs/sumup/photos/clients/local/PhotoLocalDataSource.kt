package com.benhurqs.sumup.photos.clients.local

import android.content.Context
import com.benhurqs.sumup.photos.domains.entities.Photo
import com.benhurqs.sumup.photos.managers.PhotoDataSource
import io.reactivex.Observable

class PhotoLocalDataSource(context: Context): PhotoDataSource {

    private val photoDao: PhotoDao

    companion object {
        private var mInstance: PhotoLocalDataSource? = null

        @Synchronized
        fun getInstance(context: Context): PhotoLocalDataSource {
            if(mInstance == null){
                mInstance = PhotoLocalDataSource(context)
            }
            return mInstance!!
        }
    }

    init {
        photoDao = PhotoRoomDatabase.getDatabase(context).photoDao()
    }


    override fun getPhotoList(albumID: Int): Observable<List<Photo>?> {
        return photoDao.getPhotoByAlbumId(albumID)
    }

    fun savePhotos(photo: Photo){
        PhotoRoomDatabase.databaseWriteExecutor.execute {
            photoDao.insert(photo)
        }
    }
}