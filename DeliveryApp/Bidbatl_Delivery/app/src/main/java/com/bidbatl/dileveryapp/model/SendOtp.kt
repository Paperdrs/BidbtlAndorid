package com.bidbatl.dileveryapp.model

import com.bidbatl.dileveryapp.model.data.AuthData

data class SendOtp(val code:Int,
val message:String,
val data: AuthData?)

//{
//    "code": 200,
//    "message": "Success",
//    "metadata": null,
//    "data": {
//        "otp": 8365
//    }
//}