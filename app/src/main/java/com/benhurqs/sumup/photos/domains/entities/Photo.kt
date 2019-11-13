package com.benhurqs.sumup.photos.domains.entities

import java.io.Serializable

class Photo : Serializable{
    var id: Int = 0
    var url: String? = null
    var albumID: Int = 0
}