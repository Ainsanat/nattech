package com.example.azimuth

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.azimuth.fragments.ControlFragment
import com.sjapps.library.customdialog.CustomViewDialog
import java.util.Calendar
import java.util.Date
import androidx.core.graphics.toColorInt
import java.util.ArrayList

class DeviceAdapter(private val device: ArrayList<Device>) : RecyclerView.Adapter<CardViewHolder>() {

//    private val customViewDialog = CustomViewDialog()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_item, parent, false)
        val holder = CardViewHolder(view)

        /*
        holder.imageTrash.setOnClickListener {
            customViewDialog.Builder(parent.context).apply {
                setTitle("Delete this machine?")
                dialogWithTwoButtons()
                setLeftButtonColor("#FF0000".toColorInt())
                setRightButtonColor("#008000".toColorInt())
                onButtonClick {
                    val pos = holder.adapterPosition
                    deleteItem(pos, parent.context)
                    dismiss()
                }
                show()
            }
        }

         */
        return holder
    }

    override fun getItemCount(): Int {
        return device.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = device[position]
        holder.textName.text = currentItem.name
        holder.textID.text = currentItem.clientID
    }

    /*
    private fun deleteItem(position: Int, context: Context) {
        device.moveToPosition(position)
        val id = device.getInt(0)
        val sqliteHelper = SQLiteHelper.getInstance(context)
        val db = sqliteHelper.writableDatabase
        val sql = "DELETE FROM machine WHERE _id = $id"
        db.execSQL(sql)

//        val a = ChartFragment.newInstance("Adapter")
//        a.readDatabaseToRecyclerview()

    }

    override fun getItemCount(): Int {
        return device.count
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        device.moveToPosition(position)

        holder.textName.text = device.getString(1)

        val time = device.getLong(4)
        val calendar = Calendar.getInstance()
        calendar.time = Date(time)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dayStr = if (day >= 10) "$day" else "0$day"

        holder.textDate.text = dayStr

        val month = getMonthName(calendar.get(Calendar.MONTH))
//        val year = calendar.get(Calendar.YEAR) + 543
        val year = calendar.get(Calendar.YEAR)
        holder.textID.text = "$month $year"

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val fragment = v!!.context as AppCompatActivity
                val controller = ControlFragment()
                fragment.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, controller).addToBackStack(null).commit()
            }
        })

    }

    private fun getMonthName(month: Int): String? {
        val m = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        return if (month in 0..11) m[month] else null
    }

     */
}