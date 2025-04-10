package com.location.location.data.remote

import android.content.Context
import com.location.location.constants.Const
import com.location.location.constants.Const.PARAM_APP_VERSION
import com.location.location.constants.Const.PARAM_DEVICE_TYPE
import com.location.location.models.BaseResponse
import com.location.location.models.FeaturedModel
import com.location.location.models.FiltersModel
import com.location.location.models.HomeCategoryModel
import com.location.location.models.HomeResponseModel
import com.location.location.models.LicenseModel
import com.location.location.models.SimulationModel
import com.location.location.models.UserLoginModel
import com.location.location.models.UserModel
import com.location.location.utils.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiServiceImpl @Inject constructor(
    private val apiServices: ApiServices,
    @ApplicationContext val context: Context
) : ApiHelper {

    @Inject
    lateinit var mPreferenceManager: PreferenceManager

    override suspend fun apiUserRegister(param: HashMap<String, Any?>): BaseResponse<Any?> = apiServices.apiUserRegister(addKey(param))


    private fun addKey(params: HashMap<String, Any?>): HashMap<String, Any?> {
        /*if (mPreferenceManager.getStringPreference(PreferenceManager.FCM_TOKEN).toString().isNotEmpty()) {
            params[PARAM_DEVICE_TOKEN] = mPreferenceManager.getStringPreference(PreferenceManager.FCM_TOKEN)
        } else {
            params[PARAM_DEVICE_TOKEN] = 123456
        }*/
        params[PARAM_DEVICE_TYPE] = Const.DEVICETYPE.toString()
        params[PARAM_APP_VERSION] = "1"

        /*if (mPreferenceManager.getUserLogin()) {
            params[PARAM_USER_ID] = mPreferenceManager.getUserId()
        }*/
        //Log.e("Token",mPreferenceManager.getAccessToken().toString())
        return params
    }

    private fun addKeyMultipart(params: HashMap<String, RequestBody?>): HashMap<String, RequestBody?> {
        /*if (!mPreferenceManager.getStringPreference(PreferenceManager.FCM_TOKEN).toString().isNullOrEmpty()) {
            params[PARAM_DEVICE_TOKEN] = mPreferenceManager.getStringPreference(PreferenceManager.FCM_TOKEN).toString().toReqBody()
        } else {
            params[PARAM_DEVICE_TOKEN] = "123456".toReqBody()
        }*/
        params[PARAM_DEVICE_TYPE] = Const.DEVICETYPE.toString().toReqBody()
        params[PARAM_APP_VERSION] = "1".toReqBody()

        /*if (mPreferenceManager.getUserLogin()) {
            params[PARAM_USER_ID] = mPreferenceManager.getUserId().toString().toReqBody()
        }*/
        return params
    }
}