package com.benhurqs.sumup.splash.domains.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
class User : Serializable{

    @PrimaryKey
    @NonNull
    var id: Int = 0

    var name: String? = null

    var image: String? = null
}