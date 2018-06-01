package ishopgo.com.exhibition.app

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.ProcessLifecycleOwner
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.FirebaseApp
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.util.HttpAuthorizer
import io.fabric.sdk.android.Fabric
import ishopgo.com.exhibition.BuildConfig
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import org.apache.commons.io.IOUtils

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MyApp : MultiDexApplication(), LifecycleObserver {

    lateinit var appComponent: AppComponent

    val pusher: Pusher by lazy {

        val options = PusherOptions()
        options.setCluster(Const.Chat.PUSHER_CLUSTER)
        options.isEncrypted = false
        val authorizer = HttpAuthorizer(Const.Chat.PUSHER_AUTH_ENDPOINT)
        options.authorizer = authorizer

        Pusher(Const.Chat.PUSHER_API_KEY, options)
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        // we only need crashlytic for release version, so enable it later
        if (!BuildConfig.DEBUG)
            Fabric.with(this, CrashlyticsCore())

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        UserDataManager.init(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        try {
            Const.webViewCSS = IOUtils.toString(assets.open("WebViewStyle.css"), "UTF-8")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}