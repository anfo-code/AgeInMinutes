package com.example.ageinminutes.database

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ageinminutes.DateBirthRecorder
import com.example.ageinminutes.R
import kotlinx.android.synthetic.main.item_recorder_row.view.*

class ItemAdapter(val context: Context, val items: ArrayList<RecordModel>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_recorder_row, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        holder.tvName.text = item.name
        holder.tvBirthDay.text = item.birthDate
        holder.tvRecordDate.text = item.recordDate

        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.mainBackground
                )
            )
        } else {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.rowsAddColor
                )
            )
        }

        holder.ivEdit.setOnClickListener {

            if (context is DateBirthRecorder)
                context.updateRecordDialog(item)
        }

        holder.ivDelete.setOnClickListener {
            if (context is DateBirthRecorder) {
                context.deleteRecordAlertDialog(item)
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val llMain = view.ll_history_item_main
        val tvName = view.tvName
        val tvBirthDay = view.tvBirthDate
        val tvRecordDate = view.tvRecordDate
        val ivEdit = view.ivEdit
        val ivDelete = view.ivDelete
    }

}