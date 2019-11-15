package com.benhurqs.sumup.photos.clients.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.benhurqs.sumup.photos.domains.entities.Photo
import io.reactivex.Observable

@Dao
interface PhotoDao {

    @Query("SELECT * from photos WHERE albumID=:id ORDER BY id ASC")
    fun getPhotoByAlbumId(id: Int): Observable<List<Photo>?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(photo: Photo)

    @Query("DELETE FROM photos")
    fun deleteAll()
}