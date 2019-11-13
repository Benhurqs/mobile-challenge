package com.benhurqs.sumup.photos.domains.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "albums")
class Album : Serializable {

    @PrimaryKey
    @NonNull
    var id: Int = 0

    var title: String? = null

    var userId: Int = 0
}