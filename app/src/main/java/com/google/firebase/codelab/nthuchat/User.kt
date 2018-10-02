package com.google.firebase.codelab.nthuchat

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.google.firebase.database.Query

@Entity(tableName = "user")
class User {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "div")
    var Div: String? = null

    @ColumnInfo(name = "classes")
    var Classes: String? = null

    fun getUid(): Int {
        return uid
    }

    fun setUid(uid: Int) {
        this.uid = uid
    }

    fun getDiv(): String? {
        return Div
    }

    fun setDiv(div: String?) {
        this.Div = div
    }

    fun getClasses(): String? {
        return Classes
    }

    fun setClasses(classes: String?) {
        this.Classes = classes
    }
}