package com.claffey.carefinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.claffey.carefinder.daos.HospitalDao
import com.claffey.carefinder.daos.UserDao
import com.claffey.carefinder.models.Hospital
import com.claffey.carefinder.models.User

@Database(entities = [Hospital::class, User::class], version = 1, exportSchema = false)
@TypeConverters(
    com.claffey.carefinder.database.TypeConverters.StringArrayConverter::class,
    com.claffey.carefinder.database.TypeConverters.PhoneConverter::class,
    com.claffey.carefinder.database.TypeConverters.DateConverter::class
    // com.claffey.carefinder.RoomDB.TypeConverters.AddressConverter::class
)
abstract class CareFinderRoomDb : RoomDatabase() {

    //abstract fun getLocationDoa(): LocationDao
    abstract fun getUserDao(): UserDao
    abstract fun getHospitalDao():HospitalDao

    companion object {
        @Volatile
        private var INSTANCE: CareFinderRoomDb? = null

        fun getDatabase(context: Context): CareFinderRoomDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CareFinderRoomDb::class.java,
                    "care_finder_room_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}