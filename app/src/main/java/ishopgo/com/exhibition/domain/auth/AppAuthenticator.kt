package ishopgo.com.exhibition.domain.auth

import android.app.Application
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class AppAuthenticator(app: Application): Authenticator {

    override fun authenticate(route: Route?, response: Response?): Request? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}