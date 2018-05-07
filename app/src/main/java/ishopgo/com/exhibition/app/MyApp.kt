package ishopgo.com.exhibition.app

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import org.apache.commons.io.IOUtils

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MyApp : MultiDexApplication() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        // we only need crashlytic for release version, so enable it later
//        if (!BuildConfig.DEBUG)
        Fabric.with(this, Crashlytics())

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        UserDataManager.init(this)

        try {
            Const.webViewCSS = IOUtils.toString(assets.open("WebViewStyle.css"), "UTF-8")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}