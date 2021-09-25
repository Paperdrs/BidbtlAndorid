package com.bidbatl.dileveryapp.model

import com.bidbatl.dileveryapp.model.data.OperationData

data class OperationModel(
    val code: Int,
    val message: String,
    val data: List<OperationData>?
)