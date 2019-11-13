package com.benhurqs.sumup.photos.clients.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.benhurqs.sumup.photos.domains.entities.Photo
import java.util.concurrent.Executors


@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class PhotoRoomDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object {

        @Volatile
        private var INSTANCE: PhotoRoomDatabase? = null
        private val NUMBER_OF_THREADS = 4
        internal val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        internal fun getDatabase(context: Context): PhotoRoomDatabase {
            if (INSTANCE == null) {
                synchronized(PhotoRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            PhotoRoomDatabase::class.java, "photo"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}