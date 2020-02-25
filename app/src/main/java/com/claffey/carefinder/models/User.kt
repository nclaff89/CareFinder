package com.claffey.carefinder.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    @SerializedName("_id") val id: String,
    val admin: Boolean,
    val firstName: String,
    val lastName: String,
    val email: String

)