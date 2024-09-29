package com.example.todolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.Adapter.SpinnerAdapterClass
import com.example.todolist.Room.RequestModel
import com.example.todolist.Room.ResponseModel
import com.example.todolist.rec_view.AdapterClass
import com.example.todolist.rec_view.AdapterClass.OnItemClick
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), OnItemClick {
    var recyclerView: RecyclerView? = null
    var fb: FloatingActionButton? = null
    var requestModel: RequestModel? = null
    private var dialog: AlertDialog? = null
    var adapterClass: AdapterClass? = null
    var spinner: Spinner? = null
    var list: ArrayList<ResponseModel>? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fb = findViewById(R.id.floatingbtn)
        recyclerView = findViewById(R.id.recycler_view)
        requestModel = RequestModel.getRequestModel(this@MainActivity)
        spinner = findViewById(R.id.spinner)


        fb!!.setOnClickListener(View.OnClickListener { callFloatingButtonFunction() })
        callGetDataFunction(list)
        spinnerFunction(list)
    }

    private fun spinnerFunction(list: ArrayList<ResponseModel>?) {
        val list1 = ArrayList<String>()
        list1.add("filter")
        list1.add("Recent")
        list1.add("Sequence")
        val adapterClass1 = SpinnerAdapterClass(list1, this@MainActivity)
        spinner!!.adapter = adapterClass1

        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val selectedItem = adapterView.getItemAtPosition(i) as String
                val filterList: ArrayList<ResponseModel> = data
                callFilterFunction(selectedItem, filterList)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }
    }

    private val data: ArrayList<ResponseModel>
        get() {
            val listing = requestModel!!.roomInterface()?.allData as ArrayList<ResponseModel>
            return listing
        }

    private fun callFilterFunction(selectedItem: String, list: ArrayList<ResponseModel>) {
        val filterList = ArrayList<ResponseModel>()
        if (selectedItem == "Recent") {
            for (i in list.indices.reversed()) {
                filterList.add(list[i])
            }
            spinnerFunctionData(filterList)
        } else if (selectedItem == "Sequence") {
            for (i in list.indices) {
                filterList.add(list[i])
            }
            spinnerFunctionData(filterList)
        }
    }

    private fun spinnerFunctionData(filtering: ArrayList<ResponseModel>) {
        adapterClass = AdapterClass(filtering, this@MainActivity, this@MainActivity)
        recyclerView!!.adapter = adapterClass
    }

    private fun callGetDataFunction(filterList: ArrayList<ResponseModel>?) {
        var filterList = filterList
        filterList = requestModel!!.roomInterface()?.allData as ArrayList<ResponseModel>
        adapterClass = AdapterClass(filterList, this@MainActivity, this@MainActivity)
        recyclerView!!.adapter = adapterClass
    }

    @SuppressLint("MissingInflatedId")
    private fun callFloatingButtonFunction() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.floating_dialog, null)
        val title = dialogView.findViewById<EditText>(R.id.title)
        val comments = dialogView.findViewById<EditText>(R.id.comments)
        val yes = dialogView.findViewById<Button>(R.id.yes)
        val no = dialogView.findViewById<Button>(R.id.no)
        yes.setOnClickListener {
            val Title = title.text.toString().trim { it <= ' ' }
            val Comments = comments.text.toString().trim { it <= ' ' }
            if (validation(Title, Comments)) {
                insertDataInsideTable(requestModel, Title, Comments)
                dialog!!.dismiss()
                callGetDataFunction(list)
                adapterClass!!.notifyDataSetChanged()
            }
        }
        no.setOnClickListener { dialog!!.dismiss() }
        builder.setView(dialogView)
        dialog = builder.create() // Assign dialog here
        dialog!!.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun insertDataInsideTable(
        requestModel: RequestModel?,
        title: String,
        comments: String,
    ) {
        val responseModel = ResponseModel(title, comments)
        requestModel!!.roomInterface()?.insertTheEntity(responseModel)
        adapterClass!!.notifyItemInserted(adapterClass!!.itemCount)
        adapterClass!!.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun itemClickListner(id: Int) {
        val builder = AlertDialog.Builder(this)

        // Set the dialog title, message, and other properties
        builder.setTitle("Delete")
            .setMessage("Are you sure to remove it?")
            .setPositiveButton("OK") { dialog, which -> // This is the code that will be executed when the user clicks 'OK'
                // You can add your action here
                val responseModel = ResponseModel(id)
                requestModel!!.roomInterface()?.deleteTheEntity(responseModel)
                callGetDataFunction(list)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, which -> // This is the code that will be executed when the user clicks 'Cancel'
                dialog.dismiss()
            }

        // Create and show the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    override fun updateList(id: Int, utitle: String?, ucomments: String?) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.floating_dialog, null)

        // Get references to UI elements in the dialog
        val title = dialogView.findViewById<EditText>(R.id.title)
        val comments = dialogView.findViewById<EditText>(R.id.comments)
        val yes = dialogView.findViewById<Button>(R.id.yes)
        val no = dialogView.findViewById<Button>(R.id.no)

        // Assign values to EditText, handling null safely
        title.setText(utitle ?: "")
        comments.setText(ucomments ?: "")

        // Set up Yes button click listener
        yes.setOnClickListener {
            val Title = title.text.toString().trim()
            val Comments = comments.text.toString().trim()

            // Perform validation and update the entity in the database
            if (validation(Title, Comments)) {
                val responseModel = ResponseModel(id, Title, Comments)
                requestModel?.roomInterface()?.updateTheEntity(responseModel)

                // Dismiss the dialog after updating
                dialog?.dismiss()

                // Refresh the data and notify the adapter
                callGetDataFunction(list)
                adapterClass?.notifyDataSetChanged()
            }
        }

        // Set up No button click listener
        no.setOnClickListener {
            dialog?.dismiss()
        }

        // Create and show the dialog
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()
    }

    private fun validation(title: String, comments: String): Boolean {
        if (title.isEmpty()) {
            toast("Enter Title")
            return false
        }
        if (comments.isEmpty()) {
            toast("Enter Comment")
            return false
        }
        return true
    }

    private fun toast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }


}