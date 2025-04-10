package com.location.location.data.repositories

import com.location.location.data.remote.ApiHelper
import com.location.location.data.remote.ViewState
import com.location.location.models.BaseResponse
import com.location.location.models.LicenseModel
import com.location.location.models.UserLoginModel
import com.location.location.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun apiUserRegister(param: HashMap<String, Any?>): Flow<ViewState<BaseResponse<Any?>>> {
        return flow {
            val response = apiHelper.apiUserRegister(param)
            emit(ViewState.success(response))
        }.flowOn(Dispatchers.IO)
    }
}