package com.bidbatl.dileveryapp.model

class VehicleDocuments(
    val code: Int,
    val message: String,
    val data: VehicleData?
)

data class VehicleData(
    val registration: String,
    val name: String,
    val rc_image: String,
    val insurance_image: String,
    val pollution_image: String,
    val permit_image: String,
    val engine_no:String,
    val chasis_no:String,
    val fitnes_url:String,
    val vehicle_docs:VehicleDocument
)
data class VehicleDocument(val rc_image:String,
                           val insurance_image:String,
                           val pollution_image:String,
                           val permit_image:String,
                           val fitnes_url:String)

//"data": {
//        "id": "1",
//        "driver_id": "94",
//        "name": "Abhi",
//        "registration": "DL3CNB6787",
//        "length": "10.00",
//        "width": "4.00",
//        "height": "5.00",
//        "volume": "500.00",
//        "load_capacity": "430.00",
//        "rc_number": null,
//        "rc_image": "vehicle-breakdown/200322224341397.jpg",
//        "insurance_number": null,
//        "insurance_image": "vehicle-breakdown/200322224341397.jpg",
//        "pollution_number": null,
//        "pollution_image": "vehicle-breakdown/200322224341397.jpg",
//        "filter": null,
//        "permit_number": null,
//        "permit_image": "vehicle-breakdown/200322224341397.jpg",
//        "breakdown_photo": "vehicle-breakdown/200322235420618.jpg",
//        "breakdown_message": "Message",
//        "is_breakdown": "1",
//        "status": "1",
//        "is_deleted": "0",
//        "created_at": "2020-03-17 00:00:00",
//        "updated_at": "2020-03-22 18:24:20",
//        "vehicle_docs": {
//            "rc_image": "http://134.209.144.246/api/uploads/vehicle-breakdown/200322224341397.jpg",
//            "insurance_image": "http://134.209.144.246/api/uploads/vehicle-breakdown/200322224341397.jpg",
//            "pollution_image": "http://134.209.144.246/api/uploads/vehicle-breakdown/200322224341397.jpg",
//            "permit_image": "http://134.209.144.246/api/uploads/vehicle-breakdown/200322224341397.jpg"
//        }
//    }