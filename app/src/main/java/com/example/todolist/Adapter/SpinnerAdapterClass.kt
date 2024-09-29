package com.example.todolist.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.todolist.R


class SpinnerAdapterClass(var list: ArrayList<String>, var context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(i: Int): Any {
        return list[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {
        // Check if the view is null and inflate a new one if necessary
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_xml, viewGroup, false)

        // Now find the TextView in the inflated view and set the text
        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = list[i]

        return view
    }
}

