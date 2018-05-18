package ishopgo.com.exhibition.model

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
object Const {

    const val PAGE_LIMIT = 20
    const val ID_APP = "hoichone"

    object TransferKey {
        const val EXTRA_STRING_LIST = "string_list"
        const val EXTRA_JSON = "json"
        const val EXTRA_ID = "id"
        const val EXTRA_REQUIRE = "required"
        const val EXTRA_URL = "url"
    }

    object RequestCode {
        const val EDIT_PRODUCT_COMMENT = 1
        const val CAMERA_PERMISSION = 2
        const val RC_PICK_IMAGE = 3
        const val TAKE_PICTURE = 4
        const val UPDATE_PROFILE = 5
        const val SHARE_POST_COMMUNITY = 6
        const val NOTIFICATION_ADD = 7
        const val NOTIFICATION_DETAIL = 8
        const val PRODUCT_MANAGER_ADD = 9
        const val PRODUCT_MANAGER_DETAIL = 10
        const val SALE_POINT_ADD = 11
        const val BOOTH_MANAGER_ADD = 12
        const val BRAND_MANAGER_UPDATE = 13
        const val BRAND_MANAGER_ADD = 14
        const val NEWS_MANAGER_ADD = 15
        const val NEWS_MANAGER_EDIT = 16
        const val DELETED_MEMBER_RESTORE = 17
    }

    object AccountAction {
        const val ACTION_NOT_AVALIBLE = 0
        const val ACTION_PROFILE = 1
        const val ACTION_CHANGE_PASSWORD = 2
        const val ACTION_LOGOUT = 3
        const val ACTION_REPORT = 4
        const val ACTION_SETTING_BOTTH = 5
        const val ACTION_NOTIFICATION = 6
        const val ACTION_PRODUCT_MANAGER = 7
        const val ACTION_SALE_POINT = 8
        const val ACTION_BOOTH_MANAGER = 9
        const val ACTION_MEMBER_MANAGER = 10
        const val ACTION_BRAND_MANAGER = 11
        const val ACTION_NEWS_MANAGER = 12
        const val ACTION_GENEREL_MANAGER = 13
        const val ACTION_QUESTION_MANAGER = 14
        const val ACTION_SYNTHETIC_MANAGER = 15
        const val ACTION_REGISTER_BOOTH = 16
    }

    val colors = arrayOf(
            "#845EC2",
            "#D65DB1",
            "#FF6F91",
            "#FF9671",
            "#2C73D2",
            "#008E9B",
            "#008F7A",
            "#B39CD0",
            "#C34A36",
            "#926C00"
    )

    var webViewCSS: String? = null

}