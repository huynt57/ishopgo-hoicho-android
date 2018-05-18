package ishopgo.com.exhibition.domain.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ishopgo.com.exhibition.model.chat.PreparedMessage

/**
 * Created by xuanhong on 5/18/18. HappyCoding!
 */
@Database(entities = arrayOf(PreparedMessage::class), version = 1)
abstract class AppDB : RoomDatabase() {

    abstract fun preparedMessageDao(): PreparedMessageDao

}