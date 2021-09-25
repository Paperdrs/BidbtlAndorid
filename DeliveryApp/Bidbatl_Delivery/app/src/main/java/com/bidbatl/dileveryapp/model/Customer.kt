package com.bidbatl.dileveryapp.model

import java.io.Serializable

data class Customer(val id: String,
                    val name: String,
                    val registered_name: String?,
                    val email: String?,
                    val phone: String,
                    val gst_number: String?,
                    val point: String?,
                    val photo: String?,
                    val kyc_status: String?,
                    val address: String?
): Serializable