package com.example.ageinminutes

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ageinminutes.database.DatabaseHandler
import com.example.ageinminutes.database.ItemAdapter
import com.example.ageinminutes.database.RecordModel
import com.example.ageinminutes.receiver.AlarmService
import kotlinx.android.synthetic.main.activity_date_birth_recorder.*
import kotlinx.android.synthetic.main.dialog_update.*
import kotlin.collections.ArrayList

class DateBirthRecorder : AppCompatActivity() {

    lateinit var alarmService: AlarmService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_birth_recorder)

        setToolbar()

        toolbarDateBirthRecorder.setNavigationOnClickListener {
            onBackPressed()
        }

        setupListofDataIntoRecyclerView()




    }

    private fun setToolbar() {
        setSupportActionBar(toolbarDateBirthRecorder)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
    }

    fun setupListofDataIntoRecyclerView() {
        if (getItemsList().size > 0) {

            rvRecorder.visibility = View.VISIBLE
            tvNoData.visibility = View.GONE

            rvRecorder.layoutManager = LinearLayoutManager(this)

            val itemAdapter = ItemAdapter(this, getItemsList())
            rvRecorder.adapter = itemAdapter
        } else {
            rvRecorder.visibility = View.GONE
            tvNoData.visibility = View.VISIBLE
        }
    }

    private fun getItemsList(): ArrayList<RecordModel> {
        val datebaseHandler = DatabaseHandler(this)

        val recList: ArrayList<RecordModel> = datebaseHandler.viewRecord()

        return recList
    }

    fun updateRecordDialog(recModelClass: RecordModel) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.etUpdateName.setText(recModelClass.name)
        updateDialog.etUpdateBirthday.setText(recModelClass.birthDate)

        updateDialog.tvUpdate.setOnClickListener {

            val name = updateDialog.etUpdateName.text.toString()
            val birthDay = updateDialog.etUpdateBirthday.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (name.isNotEmpty() && birthDay.isNotEmpty()) {
                val status = databaseHandler.updateRecord(
                    RecordModel(
                        recModelClass.id,
                        name,
                        birthDay,
                        recModelClass.recordDate
                    )
                )
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record Updated", Toast.LENGTH_SHORT).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Nome or Email can`t be blank",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        updateDialog.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }

    fun deleteRecordAlertDialog(recModel: RecordModel) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete Record")

        builder.setMessage("Are you sure you want to delete ${recModel.name} records?")
        builder.setIcon(android.R.drawable.ic_delete)

        builder.setPositiveButton("Yes") { dialogInterface, which ->

            val databaseHandler = DatabaseHandler(this)


            val status = databaseHandler.deleteRecord(RecordModel(recModel.id, "", "", ""))

            if (status > -1) {
                Toast.makeText(
                    applicationContext, "Record deleted succesfully.",
                    Toast.LENGTH_SHORT
                ).show()

                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss()
        }

        builder.setNegativeButton("NO") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()

    }

}