package com.benhurqs.sumup.user.clients.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.benhurqs.sumup.main.domains.entities.User
import io.reactivex.Observable


@Dao
interface UserDao {

    @Query("SELECT * from users ORDER BY id ASC")
    fun getUsers(): Observable<List<User>?>

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Query("DELETE FROM users")
    fun deleteAll()
}