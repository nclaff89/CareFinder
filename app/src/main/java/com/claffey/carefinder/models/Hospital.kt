package com.claffey.carefinder.models

/**
 * make this a data class
 */

import androidx.room.Entity
import androidx.room.PrimaryKey
//import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

@Entity(tableName = "hospital_table")
data class Hospital(
 @PrimaryKey @SerializedName("_id")
 val id: String,
 @SerializedName("county_name")
 val county: String,
 @SerializedName("phone_number")
 val phone: String,
 @SerializedName("provider_id")
 val providerID: String,
 @SerializedName("hospital_name")
 val hospitalName: String,
 @SerializedName("address")
 val address: String,
 @SerializedName("city")
 val city: String,
 @SerializedName("state")
 val state: String,
 @SerializedName("zip_code")
 val zipCode: String,
 @SerializedName("hospital_type")
 val hospitalType: String,
 @SerializedName("hospital_ownership")
 val owner: String,
 @SerializedName("emergency_services")
 val emergencyServices: Boolean,
 @SerializedName("human_address")
 val humanAddress: String,
 @SerializedName("latitude")
 val latitude: String,
 @SerializedName("longitude")
 val longitude: String
)

data class Category(val enum: MutableList<String?>)