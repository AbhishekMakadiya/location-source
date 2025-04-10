package com.location.location.constants

object Const {

    // audio
    //val AUDIO_PATH = "${App.appContext?.getExternalFilesDir(Environment.DIRECTORY_MUSIC)}${File.separator}"

    /**
     *  User Type
     **/
    //1 = Super Admin, 2 = Agent, 3 = User, 4 = Admin, 5 = Viewer, 6 = Accountant
    const val USER_TYPE_AGENT = 2
    const val USER_TYPE_USER = 3
    const val USER_TYPE_ADMIN = 4
    const val USER_TYPE_VIEWER = 5
    const val USER_TYPE_ACCOUNTANT = 6

    // Font
    const val FONT_TYPE_REGULAR = 1
    const val FONT_TYPE_MEDIUM = 2
    const val FONT_TYPE_SEMI_BOLD = 3
    const val FONT_TYPE_BOLD = 4

    const val FONT_PATH_REGULAR = "font/comfortaa_regular.ttf"
    const val FONT_PATH_MEDIUM = "font/comfortaa_medium.ttf"

    const val ITEM_TYPE_LOADING = 1
    const val ITEM_TYPE_HEADER = 2
    const val ITEM_TYPE_CONTENT = 3
    const val ITEM_TYPE_CONTENT_2 = 4

    //chat content
    const val CONTENT_TEXT = "1"
    const val CONTENT_IMAGE = "2"
    const val CONTENT_AUDIO = "3"
    const val CONTENT_VIDEO = "4"
    const val CONTENT_REQUEST = "5"

    //Media content
    const val MEDIA_TYPE_TEXT = "text"
    const val MEDIA_TYPE_AUDIO = "audio"
    const val MEDIA_TYPE_VIDEO = "video"
    const val MEDIA_TYPE_IMAGE = "image"
    const val MEDIA_TYPE_GIFT = "gift"

    const val PAGE_SIZE = 20

    const val DEVICE_ANDROID = 0


    const val MAXIMUM_IMAGE_SIZE_TO_UPLOAD =
        1024 * 1024 * 2 //2MB (Byte * Kilobyte * Megabyte).toLong()

     const val DEVICETYPE = 1
    //const val DEVICETYPE = "Android"
    const val API_KEY = 123456

    const val MULTIPART_FORM_DATA = "multipart/form-data"
    const val AD_PAGE_INTERVAL_TIME = 2500L

    // audio
    //val AUDIO_PATH = "${App.appContext?.getExternalFilesDir(Environment.DIRECTORY_MUSIC)}${File.separator}"
    const val AUDIO_EXT = ".mp3"
    const val MAX_AUDIO_LENGTH = 25000 * 1000 //  //150 * 1000 // 2.5 min
    const val MAX_BACK_MUSIC_LENGTH = 300 // 5 min (300 sec)
    const val MAX_FILE_LENGTH = 100 * 1024 * 1024 // 100 MB in bytes

    // Item Type
    const val VIEW_TYPE_ITEM = 0
    const val VIEW_TYPE_LOADING = 1


    // Response Code
    const val SUCCESS = 200
    const val SUGGESTION = 201
    const val CHARACTER_LENGTH = 200
    const val LOGOUT = 401
    const val APP_FORCE_UPDATE = 6
    const val USER_DELETED = 501
    const val BLOCKED_BY_ADMIN = 502
    const val SYNC_PAGE_SIZE = 100

    const val BEARER = "Bearer "
    const val AUTHORIZATION = BEARER + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSGFuIFNvbG8iLCJzdWIiOjQ5LCJyb2xlIjoiUk9MRV9HVUVTVCIsImlhdCI6MTY3NTY4OTY4MiwiZXhwIjoxNjc1Njg5OTgyfQ.cVryxNO1okkzovS4bi0B2rhun-B9Q7YdqspHt3TZy0w" //"somerandomstringqwerty"

    // Request Parameters
    const val PARAM_AUTHORIZATION = "Authorization"
    const val PARAM_ACCESS_TOKEN = "access-token"
    const val PARAM_USER_ID = "user_id"
    const val PARAM_ACCEPT = "Accept"
    const val PARAM_CONV_ID = "conv_id"
    const val PARAM_MESSAGE_ID = "message_id"
    const val PARAM_CLIENT_ID = "client_id"
    const val PARAM_PAGE = "page"
    const val PARAM_SIZE = "limit"
    const val PARAM_APP_VERSION = "app_version"
    const val PARAM_DEVICE_TYPE = "device_type"
    const val PARAM_DEVICE_TOKEN = "device_token"
    const val PARAM_CHAT_MEDIA = "file"

    const val PARAM_EMAIL = "email"
    const val PARAM_PASSWORD = "password"
    const val PARAM_COUNTRY = "country"
    const val PARAM_INSTITIUTE = "institiute"
    const val PARAM_DEVICE = "device"
    const val PARAM_MESSAGE = "message"
    const val PARAM_SIMULATION_ID = "simulation_id"

    const val PARAM_CONFORM_PASSWORD = "confirm_password"
    const val PARAM_NEW_PASSWORD = "new_password"
    const val PARAM_OLD_PASSWORD = "old_password"
    const val PARAM_PROFILE_IMAGE = "profile_image"
    const val PARAM_FIRST_NAME = "first_name"
    const val PARAM_FIRSTNAME = "firstname"
    const val PARAM_MIDDLE_NAME = "middle_name"
    const val PARAM_LAST_NAME = "last_name"
    const val PARAM_LASTNAME = "lastname"
    const val PARAM_DISTRICT = "district"
    const val PARAM_STATE = "state"
    const val PARAM_PINCODE = "pincode"
    const val PARAM_USER_TYPE = "user_type"
    const val PARAM_PHOTO = "photo"
    const val PARAM_BRANCH_NAME = "branch_name"
    const val PARAM_MOBILE = "mobile"
    const val PARAM_MOBILE_2 = "mobile_2"
    const val PARAM_HOME_MOBILE = "home_mobile"
    const val PARAM_ADDRESS = "address"
    const val PARAM_DESIGNATION = "designation"
    const val PARAM_RELATION = "relation"
    const val PARAM_RELATION_NAME = "relation_name"
    const val PARAM_RELATION_MOBILE = "relation_mobile"
    const val PARAM_SEARCH = "search"
    const val PARAM_NAME = "name"
    const val PARAM_USERNAME = "username"
    const val PARAM_BRANCH_ID = "branch_id"
    const val PARAM_LOAN_TYPE = "loan_type"
    const val PARAM_LOAN_AMOUNT = "loan_amount"
    const val PARAM_DOCUMENT_FILE = "documentFile"
    const val PARAM_DOC_TO_DELETE = "doc_to_delete"
    const val PARAM_APPLICANT_TYPE = "applicant_type"
    const val PARAM_RAW_CASE_ID = "raw_case_id"
    const val PARAM_RAW_CASE_APPLICANT_ID = "raw_case_applicant_id"
    const val PARAM_PICKED_CASE_ID = "picked_case_id"
    const val PARAM_RECORDS_OF = "recordsOf"
    const val PARAM_CASE_ID = "case_id"
    const val PARAM_APPLICANT_ID = "applicant_id"
    const val PARAM_POINTS_ID = "points_id"
    const val PARAM_VERIFICATION_TYPE = "verification_type"
    const val PARAM_FILTER_CASE = "filterCase"
    const val PARAM_TEMPLATE = "template"
    const val PARAM_PHONE = "phone"
    const val PARAM_OTP = "otp"
    const val PARAM_DEVICE_NAME = "device_name"
    const val PARAM_FCM_TOKEN = "fcm_token"
    const val PARAM_ID = "id"
    const val PARAM_SCRIPT_TYPE = "scriptType"
    const val PARAM_TIPPER_ID = "tipperId"
    const val PARAM_LICENSE_KEY = "license_key"
    const val PARAM_DOMAIN_ID = "domain_id"

    const val PARAM_SYMBOL = "symbol"

    // Extra parameters
    const val EXTRA_URI = "EXTRA_URI"
    const val EXTRA_DATA = "EXTRA_DATA"
    const val EXTRA_ACTION = "EXTRA_ACTION"
    const val EXTRA_FCM_TAG = "EXTRA_FCM_TAG"
    const val EXTRA_TEXT = "EXTRA_TEXT"
    const val EXTRA_TITLE = "EXTRA_TITLE"
    const val EXTRA_USER_ID = "EXTRA_USER_ID"
    const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
    const val EXTRA_COUNTRY_CODE = "EXTRA_COUNTRY_CODE"
    const val EXTRA_MOBILE = "EXTRA_MOBILE"
    const val EXTRA_EMAIL = "EXTRA_EMAIL"
    const val EXTRA_LOGIN_TYPE = "EXTRA_LOGIN_TYPE"
    const val EXTRA_AUTH_KEY = "EXTRA_AUTH_KEY"
    const val EXTRA_IS_FROM = "EXTRA_IS_FROM"
    const val EXTRA_IS_VERIFY_FOR = "EXTRA_IS_VERIFY_FOR"
    const val EXTRA_IS_CHANGE_FOR_SYS_PASSWORD = "EXTRA_IS_CHANGE_FOR_SYS_PASSWORD"
    const val EXTRA_BRANCH_DATA = "EXTRA_BRANCH_DATA"
    const val EXTRA_APPLICANT_DATA = "EXTRA_APPLICANT_DATA"
    const val EXTRA_RAW_CASE_ID = "EXTRA_RAW_CASE_ID"
    const val EXTRA_NO_OF_DOCS = "EXTRA_NO_OF_DOCS"
    const val EXTRA_TEMPLATE_DATA = "EXTRA_TEMPLATE_DATA"
    const val SITE_LIST = "SITE_LIST"
    const val USER_SITE_LIST = "USER_SITE_LIST"
    const val COMBINED_STRING = "COMBINED_STRING"
    const val ARRAYLIST = "ARRAYLIST"
    const val ATTACHMENT = "ATTACHMENT"
    const val APPLICANT_NAME = "APPLICANT_NAME"
    const val DATE = "DATE"
    const val USERDATA = "USERDATA"

    // Query Parameters
    const val QUERY_TYPE = "type"
    const val QUERY_USER_ID = "user_id"

    // Language param
    const val CUSTOM_INTENT_CHANGE_LANGUAGE = "custom.intent.action.ChangeLanguage"
    const val LOCALE_ENGLISH = 1
    const val LOCALE_ARABIC = 2

    const val MEDIA_SOURCE_CAMERA = 1
    const val MEDIA_SOURCE_GALLERY = 2

    const val UAE_COUNTRY_CODE = "971"

    const val FIRSTTAB = "FIRSTTAB"

    /**
     * broadcast receivers intent filter
     * */
    const val INTENT_CONV_ID = "com.location.location.INTENT_CONV_ID"
    const val INTENT_USER_ID = "com.location.location.INTENT_USER_ID"
    const val INTENT_USER_NAME = "com.location.location.INTENT_USER_NAME"
    const val INTENT_PROFILE = "com.location.location.INTENT_PROFILE"
    const val INTENT_BOT = "com.location.location.INTENT_BOT"
    const val INTENT_GROUP = "com.location.location.INTENT_GROUP"
    const val INTENT_GENDER = "com.location.location.INTENT_GENDER"


    const val ACTION_SESSION_EXPIRE = "android.intent.action.SESSION_EXPIRE"
    const val ACTION_USER_DELETED = "android.intent.action.USER_DELETED"
    const val INTENT_REFRESH_SCREEN = "android.intent.action.ACTION_REFRESH_SCREEN"
    const val ACTION_NEW_RAW_CASE_ADDED = "android.intent.action.ACTION_NEW_RAW_CASE_ADDED"
    const val ACTION_RAW_CASE_APPLICANT_ADD_UPDATE = "android.intent.action.ACTION_RAW_CASE_APPLICANT_ADD_UPDATE"
    const val ACTION_ACTION_TAB_SELECTED = "android.intent.action.ACTION_ACTION_TAB_SELECTED"
    const val ACTION_UPDATE_USER_DATA = "android.intent.action.UPDATE_USER_DATA"


    enum class ConversationType(val type: String, val value: String) {
        Single("1", "Single"),
        Group("2", "Group"),
    }

    enum class ConversationStatus(val conversationStatus: Int) {
        Pending (0),
        Sent (1)
    }

    enum class MessageStatus(val messageStatus: Int) {
        Pending (0),
        Sent (1),
        Received (2),
        Read (3)
    }
    enum class StarMessageStatus(val status: Int) {
        UnStar (0),
        Star (1),
    }

    enum class DeleteMessageStatus(val status: Int) {
        Pending (0),
        Sent (1),
    }

    enum class CaseType(val type: Int, val caseName : String) {
        Pickup (0,"Pickup"),
        Today (1,"Today"),
        Pending (2,"Pending"),
        Ongoing (3,"Ongoing"),
        Completed (4,"Completed"),
    }

    enum class CaseFilterType(val type: Int, val typeName : String) {
        Today (1,"Today"),
        Yesterday (2,"Yesterday"),
        ThisWeek (3,"This Week"),
        LastWeek (4,"Last Week"),
        ThisMonth (5,"This Month"),
        LastMonth (6,"Last Month"),
        ThisYear (7,"This Year"),
        LastYear (8,"Last Year"),
        All (9,"All");

        companion object {
            fun getTypeFromName(name: String?): Int? {
                return CaseFilterType.values().find { it.typeName == name }?.type
            }
        }
    }

    enum class ApplicantType(val type: Int, val typeName : String) {
        Applicant (1,"Applicant"),
        CoApplicant (2,"Co-Applicant"),
        Guarantor (3,"Guarantor");

        companion object {
            fun getTypeFromName(name: String?): Int? {
                return values().find { it.typeName == name }?.type
            }

            fun getNameFromType(type: Int?): String? {
                return values().find { it.type == type }?.typeName
            }
        }
    }

    enum class LoanType(val type: Int, val typeName : String) {
        Default (0,"Default"),
        HomeLoan (1,"Home Loan"),
        CarLoan (2,"Car Loan"),
        BusinessLoan (3,"Business Loan"),
        PersonalLoan (4,"Personal Loan");

        companion object {
            fun getTypeFromName(name: String?): Int? {
                return values().find { it.typeName == name }?.type
            }

            fun getNameFromType(type: Int?): String? {
                return ApplicantType.values().find { it.type == type }?.typeName
            }
        }
    }

    enum class Relation(val relation: String) {
        Father ("Father"),
        Mother ("Mother"),
        Brother ("Brother"),
        Sister ("Sister"),
        Wife ("Wife"),
        Other ("Other");
    }

    enum class IsFor(val type: Int) {
        AddCase (1),
        AddApplicant (2),
        UpdateApplicant (3),
    }

    enum class ScriptType(val type: Int) {
        Free (3),
        LiveTrades (1),
        ClosedTrades (2),
    }

    enum class Type(val type: Int, val typeName : String) {
        Student (1,"Student"),
        Teacher (2,"Teacher"),
        PhysicsEnthusiast (3,"Physics enthusiast");

        companion object {
            fun getTypeFromName(name: String?): Int? {
                return values().find { it.typeName == name }?.type
            }

            fun getNameFromType(type: Int?): String? {
                return values().find { it.type == type }?.typeName
            }
        }
    }

    const val CONTACTS_READ_REQ_CODE=1

    const val DIR_CASE_FILE = "CaseFile"
    const val DIR_TEMP_IMAGE_FILE = "TempImageFile"
    const val PLACES_API_KEY = "AIzaSyBSNyp6GQnnKlrMr7hD2HGiyF365tFlK5U"

}
