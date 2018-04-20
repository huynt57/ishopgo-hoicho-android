package ishopgo.com.exhibition.model

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
object UserDataManager {
    private const val PREF_NAME = "user_data_manager"

    var accessToken: String = ""
    var currentUserId: String = ""
    var currentUserPhone: String = ""
    var displayWidth: Int = 0
    var displayHeight: Int = 0

    private fun getPrefs(context: Context): SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private lateinit var pref: SharedPreferences

    fun init(context: Context) {
        pref = getPrefs(context)

        val displayMetrics = context.resources.displayMetrics
        displayWidth = displayMetrics.widthPixels
        displayHeight = displayMetrics.heightPixels
    }

}