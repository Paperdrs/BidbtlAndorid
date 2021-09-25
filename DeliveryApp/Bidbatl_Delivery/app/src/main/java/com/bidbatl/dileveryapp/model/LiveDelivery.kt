package com.bidbatl.dileveryapp.model

data class LiveDelivery
    (
    val code: Int,
    val message: String,
    val data: DeliveryData?
)

data class DeliveryData(
    val id: String,
    val user_id: String,
    val vehicle_id: String,
    val shipping_address_id: String,
    val billing_address_id: String,
    val number: String,
    val total_amount: String,
    val payment_status: String,
    val status: String,
    val bill_url: String,
    val point_used: String,
    val is_invoice: String,
    val is_challan: String,
    val invoice_no: String?,
    val invoice_date: String?,
    val is_arrival: String?,
    val created_at: String,
    val updated_at: String,
    val shipping_latitude: String,
    val shipping_longitude: String,
    val billing_latitude: String,
    val billing_longitude: String,
    val customer_name : String,
    val delivery_no:Int,
    val type:String,
    val invoice_url:String?,
    val load_capacity:Float?,
    val address : Address,
    val is_unloading_start:String,
    val is_unloading_end:String
)