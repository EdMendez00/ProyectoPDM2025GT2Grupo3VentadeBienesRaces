package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.dao.ImagenPropiedadDao
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.dao.PropiedadDao
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.ImagenPropiedadEntity
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.PropiedadEntity

@Database(
    entities = [PropiedadEntity::class, ImagenPropiedadEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun propiedadDao(): PropiedadDao
    abstract fun imagenPropiedadDao(): ImagenPropiedadDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "propiedades_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
