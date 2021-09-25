package com.bidbatl.dileveryapp.ui.callback

import com.bidbatl.dileveryapp.model.Customer

interface CustomerCallback {
    fun filterByCustomerName(operationType:Int,customer:Customer)
}