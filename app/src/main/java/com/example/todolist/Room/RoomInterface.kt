package com.example.todolist.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RoomInterface {
    @Insert
    fun insertTheEntity(responseModel: ResponseModel?)

    @Update
    fun updateTheEntity(responseModel: ResponseModel?)


    @Delete
    fun deleteTheEntity(responseModel: ResponseModel?)

    @get:Query("select * from TodoTable")
    val allData: List<ResponseModel?>?
}
