package com.example.todolist.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.todolist.Room.ResponseModel

@Database(entities = [ResponseModel::class], exportSchema = false, version = 1)
abstract class RequestModel : RoomDatabase() {
    abstract fun roomInterface(): RoomInterface?

    companion object {
        private const val DB_NAME = "ToDoList"
        private var requestModel: RequestModel? = null

        @Synchronized
        fun getRequestModel(context: Context?): RequestModel? {
            if (requestModel == null) {
                requestModel = databaseBuilder(context!!, RequestModel::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return requestModel
        }
    }
}
