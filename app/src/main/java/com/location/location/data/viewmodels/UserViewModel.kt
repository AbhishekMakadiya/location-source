package com.location.location.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.location.location.data.remote.Status
import com.location.location.data.remote.ViewState
import com.location.location.data.repositories.UserRepository
import com.location.location.models.BaseResponse
import com.location.location.models.LicenseModel
import com.location.location.models.UserLoginModel
import com.location.location.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    /**
     *
     * user Register
     * */

    private val _userRegister = MutableStateFlow(
        ViewState(Status.INITIATE, BaseResponse<Any?>(), "", null)
    )
    val userRegister: MutableStateFlow<ViewState<BaseResponse<Any?>>> get() = _userRegister
    fun apiUserRegister(param: HashMap<String, Any?>) {
        _userRegister.value = ViewState.loading()
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                userRepository.apiUserRegister(param)
                    .catch {
                        _userRegister.value = ViewState.error(it)
                    }
                    .collect {
                        _userRegister.value = ViewState.success(it.data)
                    }
            }
        }
    }
}