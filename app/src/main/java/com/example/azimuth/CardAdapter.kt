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

class CardAdapter(private val data: Cursor) : RecyclerView.Adapter<CardViewHolder>() {

    private val customViewDialog = CustomViewDialog()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_item, parent, false)
        val holder = CardViewHolder(v)

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
//        holder.itemView.setOnClickListener {
//            val intent = Intent(v.context, ControlFragment::class.java)
//            v.context.startActivity(intent)
//        }

        return holder
    }

    private fun deleteItem(position: Int, context: Context) {
        data.moveToPosition(position)
        val id = data.getInt(0)
        val sqliteHelper = SQLiteHelper.getInstance(context)
        val db = sqliteHelper.writableDatabase
        val sql = "DELETE FROM machine WHERE _id = $id"
        db.execSQL(sql)

//        val a = ChartFragment.newInstance("Adapter")
//        a.readDatabaseToRecyclerview()

    }

    override fun getItemCount(): Int {
        return data.count
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        data.moveToPosition(position)

        holder.textName.text = data.getString(1)

        val time = data.getLong(4)
        val calendar = Calendar.getInstance()
        calendar.time = Date(time)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dayStr = if (day >= 10) "$day" else "0$day"

        holder.textDate.text = dayStr

        val month = getMonthName(calendar.get(Calendar.MONTH))
//        val year = calendar.get(Calendar.YEAR) + 543
        val year = calendar.get(Calendar.YEAR)
        holder.textMonthYear.text = "$month $year"

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
}