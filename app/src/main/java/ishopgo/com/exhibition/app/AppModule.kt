package ishopgo.com.exhibition.app

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Steve
 * @since 10/22/17
 */
@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication() = application
}