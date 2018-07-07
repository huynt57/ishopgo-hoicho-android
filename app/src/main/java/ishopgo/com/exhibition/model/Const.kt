package ishopgo.com.exhibition.model

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
object Const {

    const val PAGE_LIMIT = 20
    const val ID_APP = "hoichone"

    object TransferKey {
        const val EXTRA_STRING_LIST = "string_list"
        const val EXTRA_FETCH_FULL_SIZE = "fetch_full_size"
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
        const val BOOTH_MANAGER_DELETE = 24
        const val BOOTH_FOLLOW = 25
        const val PRODUCT_FOLLOW = 26
        const val STORAGE_PERMISSION = 27
        const val DELETED_MEMBER = 28
        const val ADMINISTRATOR_ADD = 29
        const val ADMINISTRATOR_EDIT = 30
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
        const val ACTION_CONFIG_EXPO = 24
        const val ACTION_ADMINISTRATOR = 25
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

    var listPermission = mutableListOf<String>()

    object Permission {
        const val ADD_BRANCH = "add-branch"
        const val ADD_BRAND = "add-brand"
        const val ADD_CATEGORY_CONFIG = "add-category-config"
        const val ADD_CATEGORY_INFO = "add-category-info"
        const val ADD_CATEGORY_INFORMATION = "add-category-information"
        const val ADD_CATEGORY_NEW = "add-category-new"
        const val ADD_CATEGORY_QAS = "add-category-qas"
        const val ADD_CATEGORY_TRAINNING = "add-category-trainning"
        const val ADD_CHECK_WAREHOUSE = "add-check-warehouse"
        const val ADD_DEPARTMENT_MANAGER = "add-department-manager"
        const val ADD_DISCOUNT_CODE = "add-discount-code"
        const val ADD_DOCUMENT = "add-document"
        const val ADD_EXPORT_VOUCHER_WAREHOUSE = "add-export-voucher-warehouse"
        const val ADD_FUND = "add-fund"
        const val ADD_HOTLINE = "add-hotline"
        const val ADD_IMPORT_VOUCHER_WAREHOUSE = "add-import-voucher-warehouse"
        const val ADD_NEW = "add-new"
        const val ADD_NOTIFICATION = "add-notification"
        const val ADD_ORDER = "add-order"
        const val ADD_ORDER_BACK = "add-order-back"
        const val ADD_PAYMENT_COMMISSION = "add-payment-commission"
        const val ADD_PLAN_DISTRIBUTION = "add-plan-distribution"
        const val ADD_POLICY = "add-policy"
        const val ADD_POST_INFORMATION = "add-post-information"
        const val ADD_POTENTIAL_CUSTOMER = "add-potential-customer"
        const val ADD_PRODUCT = "add-product"
        const val ADD_PROVIDER = "add-provider"
        const val ADD_RECRUITMENT = "add-recruitment"
        const val ADD_SALARY_TABLE = "add-salary-table"
        const val ADD_SALE_POINT_DISTRIBUTION = "add-sale-point-distribution"
        const val ADD_STAFF_DISTRIBUTION = "add-staff-distribution"
        const val ADD_STORE_WAREHOUSE = "add-store-warehouse"
        const val ADD_SUPERVISOR = "add-supervisor"
        const val ADD_TEAM_MANAGER = "add-team-manager"
        const val ADD_TITLE_MANAGER = "add-title-manager"
        const val ADD_TRAINNING = "add-trainning"
        const val ADD_TRANSFER_VOUCHER_WAREHOUSE = "add-transfer-voucher-warehouse"
        const val ADD_VOTE_WORK = "add-vote-work"
        const val AUTO_EMAIL_MARKETING = "auto-email-marketing"
        const val BUSINESS_STATISTICS = "business-statistics"
        const val BUY_DEBT = "buy-debt"
        const val CAMPAIGN_EMAIL_MARKETING = "campaign-email-marketing"
        const val CARE_CUSTOMER = "care-customer"
        const val CATEGORY_CONFIG = "category-config"
        const val CHANGE_CARE_CUSTOMER = "change-care-customer"
        const val CHANGE_KHO_ORDER = "change-kho-order"
        const val CHANGE_POTENTIAL_CUSTOMER = "change-potential-customer"
        const val CHANGE_STATUS_KHO_ORDER = "change-status-kho-order"
        const val CHANGE_STATUS_ORDER = "change-status-order"
        const val CHANGE_TRANSPORT_ORDER = "change-transport-order"
        const val CHECK_HISTORY_WAREHOUSE = "check-history-warehouse"
        const val CHECK_WAREHOUSE = "check-warehouse"
        const val COLLECT_MONEYMANAGER = "collect-moneyManager"
        const val DELETE_BRANCH = "delete-branch"
        const val DELETE_BRAND = "delete-brand"
        const val DELETE_CATEGORY_CONFIG = "delete-category-config"
        const val DELETE_CATEGORY_INFO = "delete-category-info"
        const val DELETE_CATEGORY_NEW = "delete-category-new"
        const val DELETE_CATEGORY_QAS = "delete-category-qas"
        const val DELETE_CATEGORY_TRAINNING = "delete-category-trainning"
        const val DELETE_CHECK_WAREHOUSE = "delete-check-warehouse"
        const val DELETE_DOCUMENT = "delete-document"
        const val DELETE_FUND = "delete-fund"
        const val DELETE_HOTLINE = "delete-hotline"
        const val DELETE_MEMBER = "delete-member"
        const val DELETE_MONEYMANAGER = "delete-moneyManager"
        const val DELETE_NEW = "delete-new"
        const val DELETE_NOTIFICATION = "delete-notification"
        const val DELETE_ORDER = "delete-order"
        const val DELETE_PLAN_DISTRIBUTION = "delete-plan-distribution"
        const val DELETE_POLICY = "delete-policy"
        const val DELETE_POST_INFORMATION = "delete-post-information"
        const val DELETE_PRODUCT = "delete-product"
        const val DELETE_PROVIDER = "delete-provider"
        const val DELETE_QAS = "delete-qas"
        const val DELETE_RECRUITMENT = "delete-recruitment"
        const val DELETE_STORE_WAREHOUSE = "delete-store-warehouse"
        const val DELETE_TRAINNING = "delete-trainning"
        const val DELETE_VOUCHER_WAREHOUSE = "delete-voucher-warehouse"
        const val DEPARTMENT_MANAGER = "department-manager"
        const val DONHANG_CONFIG = "donhang-config"
        const val EDIT_BRANCH = "edit-branch"
        const val EDIT_BRAND = "edit-brand"
        const val EDIT_BUY_DEBT = "edit-buy-debt"
        const val EDIT_CATEGORY_CONFIG = "edit-category-config"
        const val EDIT_CATEGORY_INFO = "edit-category-info"
        const val EDIT_CATEGORY_INFORMATION = "edit-category-information"
        const val EDIT_CATEGORY_NEW = "edit-category-new"
        const val EDIT_CATEGORY_QAS = "edit-category-qas"
        const val EDIT_CATEGORY_TRAINNING = "edit-category-trainning"
        const val EDIT_DEPARTMENT_MANAGER = "edit-department-manager"
        const val EDIT_EMPLOYEE_PROFILE = "edit-employee-profile"
        const val EDIT_FUND = "edit-fund"
        const val EDIT_HOTLINE = "edit-hotline"
        const val EDIT_KPI_WORK = "edit-kpi-work"
        const val EDIT_MEMBER = "edit-member"
        const val EDIT_NEW = "edit-new"
        const val EDIT_NOTIFICATION = "edit-notification"
        const val EDIT_PLAN_DISTRIBUTION = "edit-plan-distribution"
        const val EDIT_POLICY = "edit-policy"
        const val EDIT_POST_INFORMATION = "edit-post-information"
        const val EDIT_POTENTIAL_CUSTOMER = "edit-potential-customer "
        const val EDIT_PRODUCT = "edit-product"
        const val EDIT_PROVIDER = "edit-provider"
        const val EDIT_PURCHASE_DEBT = "edit-purchase-debt"
        const val EDIT_QAS = "edit-qas"
        const val EDIT_RECRUITMENT = "edit-recruitment"
        const val EDIT_STORE_WAREHOUSE = "edit-store-warehouse"
        const val EDIT_TEAM_MANAGER = "edit-team-manager"
        const val EDIT_TITLE_MANAGER = "edit-title-manager"
        const val EDIT_TRAINNING = "edit-trainning"
        const val EDIT_VOTE_WORK = "edit-vote-work"
        const val EDIT_WORK = "edit-work"
        const val EMPLOYEE_PROFILE = "employee-profile"
        const val EXPO_ADD_SURVEY = "expo-add-survey"
        const val EXPO_EDIT_SURVEY = "expo-edit-survey"
        const val EXPO_FAIR_ADD = "expo-fair-add"
        const val EXPO_FAIR_DELETE = "expo-fair-delete"
        const val EXPO_FAIR_EDIT = "expo-fair-edit"
        const val EXPO_FAIR_LIST = "expo-fair-list"
        const val EXPO_FAIR_SETUP = "expo-fair-setup"
        const val EXPO_LIST_SURVEY = "expo-list-survey"
        const val EXPO_LIST_TICKET = "expo-list-ticket"
        const val EXPO_MAP_ADD = "expo-map-add"
        const val EXPO_MAP_DELETE = "expo-map-delete"
        const val EXPO_MAP_LIST = "expo-map-list"
        const val EXPORT_ORDER = "export-order"
        const val EXPORT_POTENTIAL_CUSTOMER = "export-potential-customer"
        const val FULL_PAYMENT_SALARY = "full-payment-salary"
        const val GENERAL_CONFIG = "general-config"
        const val HISTORY_ADJUST_WAREHOUSE = "history-adjust-warehouse"
        const val HISTORY_COMMISSION = "history-commission"
        const val HISTORY_DEBT = "history-debt"
        const val HISTORY_MONEYMANAGER = "history-moneyManager"
        const val HISTORY_PAYMENT_COMMISSION = "history-payment-commission"
        const val HISTORY_PAYMENT_SALARY = "history-payment-salary"
        const val HISTORY_SALARY = "history-salary"
        const val IMPORT_POTENTIAL_CUSTOMER = "import-potential-customer"
        const val INFORMATION = "information"
        const val LIST_PLAN_DISTRIBUTION = "list-plan-distribution"
        const val LIST_PRODUCT = "list-product"
        const val LIST_SUPERVISOR = "list-supervisor"
        const val MANAGER_BRANCH = "manager-branch"
        const val MANAGER_BRAND = "manager-brand"
        const val MANAGER_CATEGORY_INFO = "manager-category-info"
        const val MANAGER_DISCOUNT_CODE = "manager-discount-code"
        const val MANAGER_DOCUMENT = "manager-document"
        const val MANAGER_ERROR_TECH = "manager-error-tech"
        const val MANAGER_FIND_SYSTEM = "manager-find-system"
        const val MANAGER_FUND = "manager-fund"
        const val MANAGER_HOTLINE = "manager-hotline"
        const val MANAGER_INSTOCK_WAREHOUSE = "manager-instock-warehouse"
        const val MANAGER_MEMBER = "manager-member"
        const val MANAGER_NEW = "manager-new"
        const val MANAGER_ORDER = "manager-order"
        const val MANAGER_OUTPUT = "manager-output"
        const val MANAGER_POLICY = "manager-policy"
        const val MANAGER_PROVIDER = "manager-provider"
        const val MANAGER_QAS = "manager-qas"
        const val MANAGER_RECRUITMENT = "manager-recruitment"
        const val MANAGER_TIMEKEEPER = "manager-timekeeper"
        const val MANAGER_TRAINNING = "manager-trainning"
        const val MANAGER_VOUCHER_WAREHOUSE = "manager-voucher-warehouse"
        const val MANAGER_WORK = "manager-work"
        const val NOTE_ORDER = "note-order"
        const val PAY_BUY_DEBT = "pay-buy-debt"
        const val PAY_MONEYMANAGER = "pay-moneyManager"
        const val PAYMENT_COMMISSION = "payment-commission"
        const val PAYMENT_SALARY = "payment-salary"
        const val POTENTIAL_CUSTOMER = "potential-customer"
        const val PRINT_MONEYMANAGER = "print-moneyManager"
        const val PRINT_ORDER = "print-order"
        const val PURCHASE_DEBT = "purchase-debt"
        const val RETRIEVE_PURCHASE_DEBT = "retrieve-purchase-debt"
        const val SALARY_TABLE = "salary-table"
        const val SALARY_TABLE_HISTORY = "salary-table-history"
        const val SALE_POINT_DISTRIBUTION = "sale-point-distribution"
        const val STAFF_DISTRIBUTION = "staff-distribution"
        const val STORE_WAREHOUSE = "store-warehouse"
        const val SUPERVISE_ROUTE_DISTRIBUTION = "supervise-route-distribution"
        const val TEAM_MANAGER = "team-manager"
        const val THEME_CONFIG = "theme-config"
        const val TITLE_MANAGER = "title-manager"
        const val UPDATE_MANAGER_SALE_POINT_DISTRIBUTION = "update-manager-sale-point-distribution"
        const val UPDATE_NAME_SALE_POINT_DISTRIBUTION = "update-name-sale-point-distribution"
        const val UPDATE_NOTE_HISTORY_COMMISSION = "update-note-history-commission"
        const val UPDATE_SUPERVISOR_STAFF_DISTRIBUTION = "update-supervisor-staff-distribution"
        const val VIEW_MONEYMANAGER = "view-moneyManager"
        const val VIEW_NOTIFICATION = "view-notification"

    }

    var webViewCSS: String? = null

}