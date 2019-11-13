package com.benhurqs.sumup.photos.managers

import com.benhurqs.sumup.photos.domains.entities.Photo
import io.reactivex.Observable

interface PhotoDataSource{
    fun getPhotoList(albumID: Int) : Observable<List<Photo>>
}