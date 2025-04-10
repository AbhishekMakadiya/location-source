package com.location.location.data.remote

import com.location.location.models.BaseResponse
import com.location.location.models.FeaturedModel
import com.location.location.models.FiltersModel
import com.location.location.models.HomeCategoryModel
import com.location.location.models.UserLoginModel
import com.location.location.models.HomeResponseModel
import com.location.location.models.LicenseModel
import com.location.location.models.SimulationModel
import com.location.location.models.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Url

interface ApiServices {

    // Call Api For User Register
    @FormUrlEncoded
    @POST(Api.API_USER_REGISTER)
    suspend fun apiUserRegister(@FieldMap param: HashMap<String, Any?>): BaseResponse<Any?>

}



