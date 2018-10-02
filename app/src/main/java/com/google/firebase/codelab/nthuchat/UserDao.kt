package com.google.firebase.codelab.nthuchat

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface UserDao {

    @get:Query("SELECT * FROM user")
    val all: List<User>

    @get:Query("SELECT * FROM user")
    val user: User

    @Query("SELECT * FROM user where div LIKE :div AND classes LIKE :classes")
    fun findByName(div: String, classes: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Update
    fun update(user: User)

    @Delete
    fun deleteAll(vararg users: User)

    @Delete
    fun delete(user: User)
}
