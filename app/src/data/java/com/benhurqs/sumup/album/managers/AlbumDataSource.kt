package com.benhurqs.sumup.album.managers

import com.benhurqs.sumup.main.domains.entities.Album
import io.reactivex.Observable

interface AlbumDataSource{
    fun getAlbumList(userID: Int): Observable<List<Album>?>
}