package com.claffey.carefinder.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.claffey.carefinder.models.Hospital

@Dao
interface HospitalDao {

    @Query("SELECT * FROM hospital_table ORDER BY hospitalName DESC")
    fun getAllHospitals(): LiveData<List<Hospital>>

    @Query("SELECT * FROM hospital_table WHERE id = :id LIMIT 1")
    suspend fun getHospital(id: String): List<Hospital>

    @Query("SELECT * FROM hospital_table WHERE city = :city")
    fun getHospitalByCity(city: String) : List<Hospital>

    @Query("SELECT * FROM hospital_table WHERE state = :state")
    fun getHospitalByState(state: String) : List<Hospital>

    @Query("SELECT * FROM hospital_table WHERE zipCode = :zipCode")
    fun getHospitalByZip(zipCode: String ) : List<Hospital>

    @Query("SELECT * FROM hospital_table WHERE county = :county")
    fun getHospitalByCounty(county: String ) : List<Hospital>

    @Query("SELECT * FROM hospital_table WHERE hospitalName = :hospitalName")
    fun getHospitalByName(hospitalName: String ) : List<Hospital>

    @Query("SELECT * FROM hospital_table WHERE owner = :owner")
    fun getHospitalByOwner(owner: String ) : List<Hospital>

    @Query("SELECT * FROM hospital_table WHERE emergencyServices = :emergencyServices")
    fun getHospitalByEmergency(emergencyServices: Boolean ) : List<Hospital>

    @Query("SELECT * FROM hospital_table WHERE providerID = :pid")
    fun getHospitalByProviderID(pid: String ) : List<Hospital>





    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHospital(Hospital: Hospital)

    @Update
    fun updateHospital(Hospital: Hospital)

    @Delete
    fun deleteHospital(Hospital: Hospital)
}