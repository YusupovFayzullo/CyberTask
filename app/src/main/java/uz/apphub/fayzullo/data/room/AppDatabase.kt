package uz.apphub.fayzullo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.apphub.fayzullo.data.room.dao.SignatureDao
import uz.apphub.fayzullo.data.room.entity.SignatureEntity

/**Created By Begzod Shokirov on 11/06/2024 **/

@Database(entities = [SignatureEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cardDao(): SignatureDao
}