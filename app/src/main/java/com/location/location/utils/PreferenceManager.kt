package com.location.location.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.location.location.models.InformationModel
import com.location.location.models.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(@ApplicationContext context : Context) {

    private val preferences: SharedPreferences
    private val PRIVATE_MODE = 0

    //var context: Context

    init {
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        //context = mContext
    }

    fun clearPreference() {
        preferences.edit().clear().apply()
    }

    fun removePreference(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun setStringPreference(key: String, stringValue: String) {
        preferences.edit().putString(key, stringValue).apply()
    }

    fun setBooleanPreference(key: String, booleanValue: Boolean) {
        preferences.edit().putBoolean(key, booleanValue).apply()
    }

    fun setIntPreference(key: String, intValue: Int) {
        preferences.edit().putInt(key, intValue).apply()
    }
    fun setLongPreference(key: String, intValue: Long) {
        preferences.edit().putLong(key, intValue).apply()
    }

    fun getLongPreference(key: String): Long {
        return preferences.getLong(key, 0)
    }
    fun getStringPreference(key: String): String? {
        return preferences.getString(key, "")
    }

    fun getBooleanPreference(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun getIntPreference(key: String): Int {
        return preferences.getInt(key, -1)
    }

    /**
     * get device FCM token
     */
    fun getFCMToken(): String {
        return getStringPreference(FCM_TOKEN)?:"dummy_token_123456"
    }
    /**
     * set device FCM Token
     */
    fun setFCMToken(fcmToken:String){
        preferences.edit().putString(FCM_TOKEN, fcmToken).apply()
    }

    /**
     * get user login or not
     */
    fun getUserLogin(): Boolean {
        return preferences.getBoolean(IS_USER_ALREADY_LOGIN, false)
    }
    /**
     * get user login or not
     */
    fun setUserLogin(isLogin:Boolean){
        preferences.edit().putBoolean(IS_USER_ALREADY_LOGIN, isLogin).apply()
    }

    /**
     * get Access token
     */
    fun getAccessToken(): String {
        return preferences.getString(USER_AUTH_KEY,"").toString()
    }
    /**
     * set Access Token
     */
    fun setAccessToken(accessToken:String){
        preferences.edit().putString(USER_AUTH_KEY, accessToken).apply()
    }

    /**
     * set user data model
     */
    fun setUserData(userModel: UserModel) {
        try {
            setStringPreference(USER_LOGIN_DATA, Gson().toJson(userModel))
            setUserId(userModel.id.toString())
            //setAccessToken(userModel.token.toString().takeIf { !userModel.token.isNullOrEmpty() } ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * get user data model
     */
    fun getUserData(): UserModel? {
        return Gson().fromJson(
            getStringPreference(USER_LOGIN_DATA),
            UserModel::class.java
        )
    }

    /**
     * set information data model
     */
    fun setInformationData(mData: InformationModel) {
        try {
            setStringPreference(INFORMATION_DATA, Gson().toJson(mData))
           } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * get user data model
     */
    fun getInformationData(): InformationModel? {
        return Gson().fromJson(
            getStringPreference(INFORMATION_DATA),
            InformationModel::class.java
        )
    }



    /**
     * set user id
     */

    fun getUserId(): String? {
        return preferences.getString(APPUSERID, "")
    }

    fun setUserId(userId: String) {
        preferences.edit().putString(APPUSERID, userId).apply()
    }

    /**
     * get user First time
     */
    fun getUserFirstTime(): Boolean {
        return preferences.getBoolean(IS_USER_FIRST_TIME, true)
    }

    fun setUserFirstTime(isFristTime :Boolean){
        preferences.edit().putBoolean(IS_USER_FIRST_TIME, isFristTime).apply()
    }

    companion object {
        const val PREF_NAME = "LocationSource"
        var FCM_TOKEN = "FCM_TOKEN"
        const val NOTIFICATION_ID = "NOTIFICATION_ID"
        const val FCM_TOKEN_SEND = "FCM_TOKEN_SEND"
        const val IS_USER_ALREADY_LOGIN = "IS_USER_ALREADY_LOGIN"
        const val USER_LOGIN_DATA = "USER_LOGIN_DATA"
        const val INFORMATION_DATA = "INFORMATION_DATA"
        const val USER_AUTH_KEY = "USER_AUTH_KEY"
        val IS_SYNC_DONE = "IS_SYNC_DONE"
        var NOTINIFATION = "NOTINIFATION"
        const val APPUSERID = "APPUSERID"
        const val SUPPORTCONVID = "SUPPORTCONVID"
        const val CUSTOMERCARECONVID = "CUSTOMERCARECONVID"
        const val LAST_UPDATE_TIMESTAMP = "lastUpdateTimestamp"
        const val LAST_UPDATE_TIMESTAMP_DELETE = "lastUpdateTimestampDelete"
        const val IS_CONTACT_SYNC= "IS_CONTACT_SYNC"
        const val GROUP_MEMBER_SYNC_DATE= "GROUP_MEMBER_SYNC_DATE"
        const val PROFILE_SYNC_DATE= "PROFILE_SYNC_DATE"
        const val WHATSAPP_NUMBER = "WHATSAPP_NUMBER"
        const val CURRENT_TAB_POSITION = "CURRENT_TAB_POSITION"
        const val SHARE_APP_CONTENT = "SHARE_APP_CONTENT"
        const val PERMISSION_COUNT="PERMISSION_COUNT"
        const val IS_USER_FIRST_TIME = "IS_USER_FIRST_TIME"
    }
}
