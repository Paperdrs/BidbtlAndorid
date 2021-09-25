package com.bidbatl.dileveryapp.model

data class Profile(val code:Int,
val message:String,

val data:ProfileData)


data class ProfileData(val name:String,
                       val employee_code:String,
                       val registered_name:String,
                       val email:String,
                       val phone:String,
                       val photo:String,
                       val dob:String?,
                       val dl_number:String,
                       val address:DriverAddress,
val id_proof:ProofId)

data class DriverAddress(val address:String,
                         val area:String,
                         val state:String,
                         val city:String,
                         val zip_code:String)

data class ProofId(val id_number:String,
                         val photo:String)