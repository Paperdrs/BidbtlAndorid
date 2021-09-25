package com.bidbatl.dileveryapp.model.data

import java.io.Serializable

data class OperationData(
    val id: String,
    val user_id: String,
    val vehicle_id: String,
    val shipping_address_id: String,
    val billing_address_id: String,
    val number: String,
    val total_amount: String,
    val payment_status: String,
    val payment_type:String?,
    val status: String,
    val bill_url: String,
    val point_used: String,
    val is_invoice: String,
    val is_challan: String,
    val invoice_no: String,
    val invoice_date: String,
    val invoice_url: String,
    val challan_no: String,
    val challan_date: String,
    val challan_url: String,
    val customer_name: String,
    val area:String,
    val ship_address:String?,
    val payment_advice_url:String,
    val is_unloading_start:String,
    val is_unloading_end:String
):Serializable
