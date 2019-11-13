package com.benhurqs.sumup.album.clients.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.benhurqs.sumup.photos.domains.entities.Album
import java.util.concurrent.Executors

@Database(entities = [Album::class], version = 1, exportSchema = false)
abstract class AlbumRoomDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao

    companion object {

        @Volatile
        private var INSTANCE: AlbumRoomDatabase? = null
        private val NUMBER_OF_THREADS = 4
        internal val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        internal fun getDatabase(context: Context): AlbumRoomDatabase {
            if (INSTANCE == null) {
                synchronized(AlbumRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AlbumRoomDatabase::class.java, "album"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}