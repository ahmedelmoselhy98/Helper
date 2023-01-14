




object PrefKeys {
    const val USER_DATA = "USER_DATA"
    const val LANG = "lang"
    const val DEVICE_ID = "dev_id"
    const val IS_USER_LOGGED = "IS_USER_LOGGED"
    const val IS_USER_REGISTER = "IS_REGISTER"
    const val TOKEN = "TOKEN"
    const val IS_GUEST_USER_LOGGED = "IS_GUEST_USER_LOGGED"
    const val PREFERENCES_NAME = "PREFERENCES_NAME"
    const val DEVICE_TOKEN = "DEVICE_TOKEN"
    const val LOCATION_DATA = "LOCATION_DATA"
    const val IS_LOCATION_SAVED = "IS_LOCATION_SAVED"
    const val IS_LOCATION_UPDATED = "IS_LOCATION_UPDATED"
    const val FIREBASE: String = "fcm"
    const val STATUS: String = "status"
    const val INTERVAL: String = "interval"
    const val HAS_REGISTER: String = "hasRegister"
    const val SHOW_NOTIFICATION: String = "notification_show"
    const val USER: String = "user"
    const val SESSION: String = "session"
}


object ERROR_API {
    //deleted or unauthorized 401
    const val UNAUTHRIZED = "unAuthroized"

    //500
    const val SERVER_ERROR = "serverError"

    //503
    const val MAINTENANCE = "maintenanceMode"

    //400
    const val BODY_ERROR = "body_error"

    //400
    const val BAD_REQUEST = "bad_request"

    //404
    const val NOT_FOUND = "not_found"

    //403
    const val UPDATE_APP = "update-app"

    const val USER_DELETED = "userDelete"

    //no internet connection
    const val CONNECTION_ERROR = "connection_error"

}


data class CustomErrorThrow(val key:String,val value:String):Throwable()