package ishopgo.com.exhibition.app

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.FirebaseApp
import io.fabric.sdk.android.Fabric
import ishopgo.com.exhibition.BuildConfig
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.chat.local.service.PusherChatService
import org.apache.commons.io.IOUtils

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MyApp : MultiDexApplication(), LifecycleObserver {

    companion object {
        private val TAG = "MyApp"
    }

    lateinit var appComponent: AppComponent

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

    private var isServiceBound = false
    private var mChatServiceBinder: PusherChatService.ChatBinder? = null
    private val mChatServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mChatServiceBinder = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isServiceBound = true

            mChatServiceBinder = service as PusherChatService.ChatBinder
            mChatServiceBinder?.registerChannel("private-new-channel-${UserDataManager.currentUserId}")
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        Log.d("Hong", "App in background")

        if (mChatServiceBinder != null && mChatServiceBinder!!.isBinderAlive)
            unbindChatService()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        Log.d("Hong", "App in foreground")

        bindChatService()
    }

    private fun unbindChatService() {
        if (isServiceBound && mChatServiceBinder != null) {
            unbindService(mChatServiceConnection)
            isServiceBound = false
        }
    }

    private fun bindChatService() {
        if (!isServiceBound || mChatServiceBinder == null) {
            bindService(PusherChatService.getIntent(this), mChatServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

}