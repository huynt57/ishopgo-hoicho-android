package ishopgo.com.exhibition.app

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import ishopgo.com.exhibition.domain.db.AppDB
import ishopgo.com.exhibition.domain.db.MessageRepository
import ishopgo.com.exhibition.domain.db.MessageRepositoryImpl
import javax.inject.Singleton

/**
 * Created by xuanhong on 5/18/18. HappyCoding!
 */
@Module
class RepositoryModule {


    @Singleton
    @Provides
    fun provideDatabase(application: Application): AppDB {
        return Room.databaseBuilder(application, AppDB::class.java, "expo_db.db")
                .build()
    }

    @Singleton
    @Provides
    fun provideMessageRepository(db: AppDB): MessageRepository = MessageRepositoryImpl(db)

}