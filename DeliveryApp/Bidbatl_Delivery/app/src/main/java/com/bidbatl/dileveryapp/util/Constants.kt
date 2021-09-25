package com.bidbatl.dileveryapp.util

object Constants {
    const val BASE_URL = "http://134.209.144.246/api/"
    const val MAP_BASE_URL = "https://maps.googleapis.com/maps/api/"
    //Login
    const val login = "driverLogin"
    const val requestOTP = "sendOtp"
    const val resendActivationCode = "userLogin"
    const val validateOTP = "verifyOtp"

    //Operation
    const val order = "getOrderForDriver"
    const val search = "searchOrder/"
    const val customerList = "customerList"

    // Live
    const val liveDelivery = "getLiveOrder"
    const val upload = "upload.php"
    const val acceptCashPayment = "acceptDeliveryPayment"
    const val reportArrival = "reportArrival/"
    const val reportStart = "reportUnloading/start/"
    const val reportEnd = "reportUnloading/end/"
    const val deliveryStatus = "changeOrderDeliveryStatus"

    //Vehicle Document
    const val vehicleDocument = "getDeliveryVehicleDetail/"
    //Report BreakDown
    const val reportBreakDown = "reportBreakdown"
    //Report Non Deliver
    const val reportNonDeliver = "reportNonDeliver"
    // Profile
    const val userProfile = "getDriverProfile"
    const val BOTTOM_SHEET_HT = 0.25f

    enum class PageTitle(val value: String) {
        RC("RC"),
        INVOICE("Invoice"),
        PERMIT("Permit"),
        FITNESS("Fitness"),
        POLLUTION("Pollution"),
        CHALLAN("Challan"),
        EWAYBILL("E-WayBill"),
        PAYMENTADVICE("PaymentAdvice"),
        INSURANCE("Insurance")
    }


}
