package com.location.location.data.remote

import com.location.location.models.BaseResponse
import com.location.location.models.FeaturedModel
import com.location.location.models.FiltersModel
import com.location.location.models.HomeCategoryModel
import com.location.location.models.HomeResponseModel
import com.location.location.models.LicenseModel
import com.location.location.models.SimulationModel
import com.location.location.models.UserLoginModel
import com.location.location.models.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ApiHelper{
    suspend fun apiUserRegister(param: HashMap<String, Any?>): BaseResponse<Any?>
}