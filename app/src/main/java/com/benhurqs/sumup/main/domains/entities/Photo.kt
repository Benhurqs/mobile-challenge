package com.benhurqs.sumup.main.domains.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "photos")
class Photo : Serializable{

    @PrimaryKey
    @NonNull
    var id: Int = 0

    var url: String? = null

    var albumID: Int = 0
}