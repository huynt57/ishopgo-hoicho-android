package ishopgo.com.exhibition.app

import android.support.multidex.MultiDexApplication
import ishopgo.com.exhibition.model.UserDataManager

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MyApp : MultiDexApplication() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        UserDataManager.init(this)
    }
}