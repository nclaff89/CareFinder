package com.claffey.carefinder.viewModels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.claffey.carefinder.daos.HospitalDao
import com.claffey.carefinder.daos.UserDao
import com.claffey.carefinder.database.CareFinderRoomDb
import com.claffey.carefinder.extensions.isValidEmailAddress
import com.claffey.carefinder.extensions.isValidPassword
import com.claffey.carefinder.models.Hospital
import com.claffey.carefinder.models.LoginEvent
import com.claffey.carefinder.models.SingleEvent
import com.claffey.carefinder.models.User
import com.claffey.carefinder.network.LoginEndPoints
import com.claffey.carefinder.network.RetroFitInstance
import com.claffey.carefinder.utilities.Constants.Companion.SHARED_PREFS
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private val calls = RetroFitInstance.getRetroFitInstance().create(LoginEndPoints::class.java)

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    private val userDao: UserDao = CareFinderRoomDb.getDatabase(application).getUserDao()
    private val hospitalDao: HospitalDao = CareFinderRoomDb.getDatabase(application).getHospitalDao()
    private val errorMessage = MutableLiveData<SingleEvent<Pair<String, Boolean>>>()

    private val loginEvent = MutableLiveData<SingleEvent<LoginEvent>>()

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>>
        get() = _userList

    val getErrorMessage: LiveData<SingleEvent<Pair<String, Boolean>>>
        get() = errorMessage

    val getLoginEvent: LiveData<SingleEvent<LoginEvent>>
        get() = loginEvent

    fun loginOnClick(email: String, password: String) {
        loginEvent.value =
            SingleEvent(LoginEvent.VERIFYING)
        scope.launch(Dispatchers.IO) {
            val loginCall = calls.loginAsync(email, password)
            try {
                val response = loginCall.await()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            sharedPreferences.edit().putString("token", it.token).apply()
                            loginEvent.postValue(SingleEvent(LoginEvent.LOGGED_IN))
                            getRemoteUserData(it.token)
                        }
                    }
                    else -> {
                        Log.d("LoginViewModel", "${response.code()} ${response.message()}")
                        loginEvent.postValue(SingleEvent(LoginEvent.FAILED))
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "${e.message} \n ${e.stackTrace}")
                loginEvent.postValue(SingleEvent(LoginEvent.FAILED))
            }
        }
    }

    fun registerOnClick(email: String, password: String, passwordConfirm: String) {
        when {
            email.isValidEmailAddress()
                    && password.isValidPassword()
                    && password.contentEquals(passwordConfirm) -> {
                register(email, password)
                return
            }
        }
        displayErrorMessage(("email" to email.isValidEmailAddress()))
        displayErrorMessage(("password" to password.isValidPassword()))
        displayErrorMessage(("passwordConfirm" to password.contentEquals(passwordConfirm)))
    }

    fun hasSavedToken() {
        when {
            sharedPreferences.contains("token") -> {
                scope.launch(Dispatchers.IO) {
                    sharedPreferences.getString("token", "")?.let { getRemoteUserData(it) }
                }
            }
            else -> logout()
        }
    }


    private suspend fun getRemoteUserData(token: String) {
        val userCall = calls.getRemoteUserAsync(token)
        try {
            val response = userCall.await()
            when (response.code()) {
                200 -> {
                    response.body()?.let {
                        userDao.insertUser(it)
                        sharedPreferences.edit().putString("userId", it.id).apply()
                       // updateCheckedInLocations(it.locations)
                        Log.d(this.javaClass.name, response.raw().body().toString())
                    }
                }
                else -> logout()
            }
        } catch (e: Exception) {

        }
    }




    private fun register(email: String, password: String) {
        loginEvent.value =
            SingleEvent(LoginEvent.VERIFYING)
        scope.launch(Dispatchers.IO) {
            val registerCall = calls.registerAsync("test1","test2", email, password, false)
            try {
                val response = registerCall.await()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            Log.d("LoginViewModel", response.body().toString())
                            sharedPreferences.edit().putString("token", it.token).apply()
                            loginEvent.postValue(SingleEvent(LoginEvent.LOGGED_IN))
                            getRemoteUserData(it.token)
                        }
                    }
                    else -> {
                        Log.d("LoginViewModel", response.message())
                        loginEvent.postValue(SingleEvent(LoginEvent.FAILED))
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", e.message)
                loginEvent.postValue(SingleEvent(LoginEvent.FAILED))
            }
        }
    }

    private fun displayErrorMessage(isValid: Pair<String, Boolean>) {
        errorMessage.value = SingleEvent(isValid)
    }

    override fun onCleared() {
        super.onCleared()
        displayErrorMessage(isValid = "all" to true)
    }

    fun logout() {
        if (sharedPreferences.contains("token")) {
            scope.launch(Dispatchers.IO) {
                sharedPreferences.getString("userId", "")?.let { userDao.deleteUserById(it) }
                sharedPreferences.edit().remove("token").apply()
                sharedPreferences.edit().remove("userId").apply()
            }
        }
        loginEvent.postValue(SingleEvent(LoginEvent.LOGGED_OUT))
    }

//    private var _selectedUser = MutableLiveData<Hospital>()
//    fun getAdminPriv(email: String) : LiveData<User>{
//        scope.launch(Dispatchers.IO) {
//            val user = userDao.getAdminPriv(email)
//
//        }
//
//
//    }
}