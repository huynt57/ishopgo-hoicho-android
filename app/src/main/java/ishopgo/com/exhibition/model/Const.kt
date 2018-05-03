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
        const val EXTRA_URL = "url"
    }

    object RequestCode {
        const val EDIT_PRODUCT_COMMENT = 1
        const val CAMERA_PERMISSION = 2
        const val RC_PICK_IMAGE = 3
        const val TAKE_PICTURE = 4
        const val UPDATE_PROFILE = 5
        const val SHARE_POST_COMMUNITY = 6
    }

    object AccountAction {
        const val ACTION_PROFILE = 1
        const val ACTION_CHANGE_PASSWORD = 2
        const val ACTION_LOGOUT = 3
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

}