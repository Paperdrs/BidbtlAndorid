package com.bidbatl.dileveryapp.model.googlemap

data class Distance(
    val status: String,
    val rows: List<DistanceRow>
)

data class DistanceRow(
    val elements: List<DistanceElements>
)

data class DistanceElements(
    val status: String
    , val distance: DistanceAndDuration,
    val duration: DistanceAndDuration
)

data class DistanceAndDuration(val text: String)
