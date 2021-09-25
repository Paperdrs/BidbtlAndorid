package com.bidbatl.dileveryapp.model

import com.bidbatl.dileveryapp.model.data.AuthData

data class LoginResponse(
        val message: String,
        val code: Int,
        val data: AuthData?

)