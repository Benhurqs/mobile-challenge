package com.benhurqs.sumup.photos.domains.entities

import java.io.Serializable

class Album : Serializable {
    var id: Int = 0
    var title: String? = null
    var userId: Int = 0
}