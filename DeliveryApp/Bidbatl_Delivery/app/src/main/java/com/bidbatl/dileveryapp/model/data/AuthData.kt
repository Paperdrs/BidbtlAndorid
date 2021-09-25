package com.bidbatl.dileveryapp.model.data

data class AuthData(
    val otp: Int?,
    val id: String,
    val role: String,
    val email: String?,
    val phone: String,
    val name: String,
    val registered_name: String,
    val gst_number: String?,
    val photo: String?,
    val point: String,
    val token: String,
    val vehicle_id:String
)

//"data": {
//        "id": "94",
//        "role": "driver",
//        "email": "123@gmail.com",
//        "phone": "1111111111",
//        "name": "Driver 1",
//        "registered_name": "Driver1",
//        "gst_number": "ef4afe4389fd8d9150dd6ed7fb148759",
//        "photo": null,
//        "point": "250",
//        "token": "fa8d2e0cfa1dcfabd0239a9b20beafbe"
//    }