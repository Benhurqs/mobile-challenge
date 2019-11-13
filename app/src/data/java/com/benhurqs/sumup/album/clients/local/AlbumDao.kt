package com.benhurqs.sumup.album.clients.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.benhurqs.sumup.photos.domains.entities.Album
import io.reactivex.Observable

@Dao
interface AlbumDao {

    @Query("SELECT * from albums ORDER BY id ASC")
    fun getAlbums(): Observable<List<Album>?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(album: Album)

    @Query("DELETE FROM albums")
    fun deleteAll()
}