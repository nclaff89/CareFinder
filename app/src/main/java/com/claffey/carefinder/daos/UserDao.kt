package com.claffey.carefinder.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.claffey.carefinder.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("DELETE FROM user_table WHERE id = :userId")
    suspend fun deleteUserById(userId: String)

    @Query("SELECT * FROM user_table")
    fun getUser(): LiveData<List<User>>

//    @Query("SELECT admin FROM user_Table WHERE email = :email")
//    fun getAdminPriv(email: String): LiveData<User>

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user_table LIMIT 1")
    fun getUserNoLiveData(): User

    @Update
    suspend fun updateUser(user: User)
}