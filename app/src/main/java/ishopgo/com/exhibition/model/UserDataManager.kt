package ishopgo.com.exhibition.model

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
object UserDataManager {
    private const val PREF_NAME = "user_data_manager"

    private const val KEY_APP_ID = "app_id"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_PASS_LOGIN_SCREEN = "pass_login_screen"
    private const val KEY_SKIP_UPDATE = "skip_update"
    private const val KEY_USER_AVATAR = "user_avatar"
    private const val KEY_USER_PHONE = "user_phone"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_TYPE = "type"
    private const val KEY_USER_SURVEY = "survey"
    private const val KEY_USER_SURVEY_IS_MANDATORY = "survey_mandatory"

    var accessToken: String
        get() = pref.getString(KEY_ACCESS_TOKEN, "")
        set(value) = pref.edit().putString(KEY_ACCESS_TOKEN, value).apply()
    var appId: String
        get() = pref.getString(KEY_APP_ID, "")
        set(value) = pref.edit().putString(KEY_APP_ID, value).apply()
    var currentUserAvatar: String
        get() = pref.getString(KEY_USER_AVATAR, "")
        set(value) = pref.edit().putString(KEY_USER_AVATAR, value).apply()
    var currentUserId: Long
        get() = pref.getLong(KEY_USER_ID, -1L)
        set(value) = pref.edit().putLong(KEY_USER_ID, value).apply()
    var currentUserPhone: String
        get() = pref.getString(KEY_USER_PHONE, "")
        set(value) = pref.edit().putString(KEY_USER_PHONE, value).apply()
    var currentUserName: String
        get() = pref.getString(KEY_USER_NAME, "")
        set(value) = pref.edit().putString(KEY_USER_NAME, value).apply()
    var currentType: String
        get() = pref.getString(KEY_USER_TYPE, "")
        set(value) = pref.edit().putString(KEY_USER_TYPE, value).apply()
    var currentSurvey: Int
        get() = pref.getInt(KEY_USER_SURVEY, 0)
        set(value) = pref.edit().putInt(KEY_USER_SURVEY, value).apply()
    var currentSurveyIsMandatory: Boolean
        get() = pref.getBoolean(KEY_USER_SURVEY_IS_MANDATORY, false)
        set(value) = pref.edit().putBoolean(KEY_USER_SURVEY_IS_MANDATORY, value).apply()
    var passLoginScreen: Long
        get() = pref.getLong(KEY_PASS_LOGIN_SCREEN, -1L)
        set(value) = pref.edit().putLong(KEY_PASS_LOGIN_SCREEN, value).apply()
    var skipUpdate: Boolean
        get() = pref.getBoolean(KEY_SKIP_UPDATE, false)
        set(value) = pref.edit().putBoolean(KEY_SKIP_UPDATE, value).apply()


    var displayWidth: Int = 0
    var displayHeight: Int = 0

    private fun getPrefs(context: Context): SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private lateinit var pref: SharedPreferences

    fun init(context: Context) {
        pref = getPrefs(context)

        val displayMetrics = context.resources.displayMetrics
        displayWidth = displayMetrics.widthPixels
        displayHeight = displayMetrics.heightPixels

        // TODO this field will be config by server in the future
        currentSurveyIsMandatory = false

        appId = Const.ID_APP
    }

    fun deleteUserInfo() {
        val edit = pref.edit()
        edit.remove(KEY_ACCESS_TOKEN)
        edit.remove(KEY_USER_ID)
        edit.remove(KEY_PASS_LOGIN_SCREEN)
        edit.remove(KEY_SKIP_UPDATE)
        edit.remove(KEY_USER_AVATAR)
        edit.remove(KEY_USER_PHONE)
        edit.remove(KEY_USER_NAME)
        edit.remove(KEY_USER_TYPE)
        edit.remove(KEY_USER_SURVEY)
        edit.apply()
    }

}