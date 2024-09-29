package com.example.todolist.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "TodoTable")
class ResponseModel {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "comments")
    var comments: String? = null

    @Ignore
    constructor(title: String?, comments: String?) {
        this.title = title
        this.comments = comments
    }

    @Ignore
    constructor(id: Int, title: String?, comments: String?) {
        this.id = id
        this.title = title
        this.comments = comments
    }

    constructor(id: Int) {
        this.id = id
    }
}
