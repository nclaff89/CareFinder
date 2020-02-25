package com.claffey.carefinder.viewModels

import android.content.Context
import android.content.SharedPreferences

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.claffey.carefinder.daos.HospitalDao
import com.claffey.carefinder.daos.UserDao
import com.claffey.carefinder.database.CareFinderRoomDb
import com.claffey.carefinder.models.Hospital
import com.claffey.carefinder.network.HospitalEndPoints
import com.claffey.carefinder.network.RetroFitInstance

import com.claffey.carefinder.utilities.Constants
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HospitalViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)

    private val api = RetroFitInstance.getRetroFitInstance()
    private val calls: HospitalEndPoints = api.create(HospitalEndPoints::class.java)

    private val userDao: UserDao = CareFinderRoomDb.getDatabase(application).getUserDao()
    private val HospitalDao: HospitalDao = CareFinderRoomDb.getDatabase(application).getHospitalDao()
    //private val geocoder: Geocoder = Geocoder(application)

    fun getRemoteHospitals() {
        scope.launch(Dispatchers.IO) {
            try {
                val call = calls.getHosptialsAsync(sharedPreferences.getString("token", ""))
                val response = call.await()
                when {
                    response.isSuccessful -> {
                        response.body()?.let { HospitalList ->
                            HospitalList.forEach { Hospital: Hospital? ->
                                runBlocking {
                                    Hospital?.let { insertHospital(Hospital) }
////
                               }

                            }
                        }
                    }
                    else -> {
                        Log.e(
                            this.javaClass.name,
                            "getRemoteHospitals() failed: code: ${response.code()} message: ${response.message()}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    this.javaClass.name,
                    "getRemoteHospitals() try block failed: ${e.message}"
                )
            }
        }
    }


    fun getAllHospitals(): LiveData<List<Hospital>> {
        getRemoteHospitals()
        return HospitalDao.getAllHospitals()
    }

    private var _selectedHospital = MutableLiveData<List<Hospital>>()

    fun getHospital(id: String): LiveData<List<Hospital>> {


        scope.launch(Dispatchers.IO) {
            val Hospital = HospitalDao.getHospital(id)
            _selectedHospital.postValue(Hospital)
        }
        return _selectedHospital
    }

    fun getHospitalByCity(city: String) : LiveData<List<Hospital>>{
        scope.launch (Dispatchers.IO){
            val Hospital = HospitalDao.getHospitalByCity(city)
            _selectedHospital.postValue(Hospital)
        }
        return _selectedHospital
    }

    fun getHospitalByState(state: String) : LiveData<List<Hospital>>{
        scope.launch (Dispatchers.IO){
            val Hospital = HospitalDao.getHospitalByState(state)
            _selectedHospital.postValue(Hospital)
        }
        return _selectedHospital
    }

    fun getHospitalByZip(zip: String) : LiveData<List<Hospital>>{
        scope.launch (Dispatchers.IO){
            val Hospital = HospitalDao.getHospitalByZip(zip)
            _selectedHospital.postValue(Hospital)
        }
        return _selectedHospital
    }

    fun getHospitalByCounty(county: String) : LiveData<List<Hospital>>{
        scope.launch (Dispatchers.IO){
            val Hospital = HospitalDao.getHospitalByCounty(county)
            _selectedHospital.postValue(Hospital)
        }
        return _selectedHospital
    }

    fun getHospitalByName(name: String) : LiveData<List<Hospital>>{
        scope.launch (Dispatchers.IO){
            val Hospital = HospitalDao.getHospitalByName(name)
            _selectedHospital.postValue(Hospital)
        }
        return _selectedHospital
    }
    fun getHospitalByProviderID(pid: String) : LiveData<List<Hospital>>{
        scope.launch (Dispatchers.IO){
            val Hospital = HospitalDao.getHospitalByProviderID(pid)
            _selectedHospital.postValue(Hospital)
        }
        return _selectedHospital
    }

    fun getHospitalByEmergency(emergency: Boolean) : LiveData<List<Hospital>>{
        scope.launch (Dispatchers.IO){
            val Hospital = HospitalDao.getHospitalByEmergency(emergency)
            _selectedHospital.postValue(Hospital)
        }
        return _selectedHospital
    }




    private fun insertHospital(Hospital: Hospital) {
        scope.launch(Dispatchers.IO) {
            HospitalDao.insertHospital(Hospital)
        }
    }

//    fun deleteHospital(Hospital: Hospital) {
//        scope.launch(Dispatchers.IO) {
//            HospitalDao.deleteHospital(Hospital)
//        }
//        if (Hospital == _selectedHospital.value) _selectedHospital = MutableLiveData()
//    }



    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}