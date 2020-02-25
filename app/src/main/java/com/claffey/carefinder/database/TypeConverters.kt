package com.claffey.carefinder.database

import android.provider.ContactsContract
import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*


class TypeConverters {

    class DateConverter {
        @TypeConverter
        fun dateToLong(date: Date): Long {
            return date.time
        }

        @TypeConverter
        fun longToDate(value: Long): Date {
            return Date(value)
        }
    }

    class StringArrayConverter {

        private var gson = GsonBuilder().create()
        @TypeConverter
        fun arrayToString(list: MutableList<String?>?): String {
            return gson.toJson(list)
        }

        @TypeConverter
        fun stringToArray(jsonString: String): MutableList<String?>? {
            val type = object : TypeToken<MutableList<String?>?>() {}.type
            return gson.fromJson(jsonString, type)
        }
    }

    class PhoneConverter {

        private var gson = GsonBuilder().create()
        @TypeConverter
        fun phoneToString(phone: ContactsContract.CommonDataKinds.Phone): String {
            return gson.toJson(phone)
        }

        @TypeConverter
        fun stringToPhone(jsonString: String): ContactsContract.CommonDataKinds.Phone {
            val type = object : TypeToken<ContactsContract.CommonDataKinds.Phone>() {}.type
            return gson.fromJson(jsonString, type)
        }
    }
//
//    class AddressConverter {
//
//        private var gson = GsonBuilder().create()
//        @TypeConverter
//        fun cagtegoryToString(address: Address): String {
//            return gson.toJson(address)
//        }
//
//        @TypeConverter
//        fun stringToAddress(jsonString: String): Address {
//            val type = object : TypeToken<Address>() {}.type
//            return gson.fromJson(jsonString, type)
//        }
//    }
}