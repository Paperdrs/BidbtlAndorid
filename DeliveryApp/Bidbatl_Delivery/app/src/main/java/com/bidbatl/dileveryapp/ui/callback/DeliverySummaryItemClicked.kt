package com.bidbatl.dileveryapp.ui.callback

import com.bidbatl.dileveryapp.model.data.OperationData

interface DeliverySummaryItemClicked {
    fun didClickedSummaryItem(operationModel: OperationData)
}