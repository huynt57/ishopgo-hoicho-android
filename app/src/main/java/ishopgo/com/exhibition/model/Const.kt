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
        const val EXTRA_TITLE = "title"
        const val EXTRA_CONVERSATION_ID = "conversation_id"
        const val EXTRA_REQUIRE = "required"
        const val EXTRA_URL = "url"
        const val EXTRA_ENABLE_CREATE_GROUP = "can_create_group"
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
        const val RC_PICK_IMAGES = 18
        const val RC_CAPTURE_IMAGE = 19
        const val RC_SHOW_DETAIL = 20
        const val RC_ADD_NEW = 21
        const val PRODUCT_SALE_POINT_DETAIL = 22
        const val UPDATE_PROFILE_AVATAR = 23
        const val BOOTH_MANAGER_DELETE = 24
        const val BOOTH_FOLLOW = 25
        const val PRODUCT_FOLLOW = 26
        const val STORAGE_PERMISSION = 27
        const val DELETED_MEMBER = 28
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
        const val ACTION_MY_QR = 16
        const val ACTION_REGISTER_BOOTH = 17
        const val ACTION_SURVEY = 18
        const val ACTION_TICKET = 19
        const val ACTION_FAVORITE_PRODUCTS = 20
        const val ACTION_FAVORITE_BOOTHS = 21
        const val ACTION_TICKET_MANAGER = 22
        const val ACTION_VISITORS = 23
    }

    object Chat {
        const val FILTER_NEW_MESSAGE = "chat.pusher.new_message"
        const val EXTRA_MESSAGE = "message_content"
        const val EXTRA_EVENT_NAME = "event_name"
        const val EXTRA_CHANNEL_NAME = "channel_name"
        const val BROADCAST_NOTIFICATION = "ishopgo.com.exhibition.ui.chat.local.service.BROADCAST_NOTIFICATION"
        const val PUSHER_MESSAGE = "ishopgo.com.exhibition.ui.chat.local.service.PUSH_MESSAGE"

        val PUSHER_CLUSTER = "ap1"
        val PUSHER_AUTH_ENDPOINT = "http://ishopgo.com/api/v1/chat/pusher-auth"
        val PUSHER_API_KEY = "23b43a03aec7a3b476d6"

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