package com.example.todolist.rec_view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.Room.ResponseModel
import com.example.todolist.rec_view.AdapterClass.myViewHolder

class AdapterClass(
    var list: ArrayList<ResponseModel>,
    var context: Context,
    var onItemClick: OnItemClick?
) : RecyclerView.Adapter<myViewHolder>() {
    interface OnItemClick {
        fun itemClickListner(id: Int)
        fun updateList(id: Int, title: String?, comments: String?)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_row_xml, parent, false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: myViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.textView.text = list[position].title
        holder.comments.text = list[position].comments
        holder.update.setOnClickListener {
            if (onItemClick != null) {
                onItemClick!!.updateList(
                    list[position].id,
                    list[position].title,
                    list[position].comments
                )
            }
        }
        holder.remove.setOnClickListener {
            if (onItemClick != null) {
                onItemClick!!.itemClickListner(list[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.title1)
        var comments: TextView = itemView.findViewById(R.id.comments1)
        var update: ImageView = itemView.findViewById(R.id.edit)
        var remove: ImageView = itemView.findViewById(R.id.remove)
    }
}
