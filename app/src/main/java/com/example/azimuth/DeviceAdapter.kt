package com.example.azimuth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.azimuth.fragments.DeviceFragmentDirections
import java.util.ArrayList

class DeviceAdapter(private val deviceList: ArrayList<Device>) :
    RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById(R.id.tv_name_device)
        val deviceID: TextView = itemView.findViewById(R.id.deviceID)
        val deviceItem: CardView = itemView.findViewById(R.id.item_device)
        val deviceEdit: ImageButton = itemView.findViewById(R.id.edit_device)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = deviceList[position]
        holder.deviceItem.startAnimation(AnimationUtils.loadAnimation(holder.deviceItem.context, R.anim.fall_down))
        holder.apply {
            deviceName.text = currentItem.name
            deviceID.text = currentItem.id
            deviceItem.setOnClickListener {
                val action = DeviceFragmentDirections.actionDeviceToControlFragment(
                    currentItem.id.toString(),
                    currentItem.name.toString(),
                    currentItem.clientID.toString(),
                    currentItem.token.toString(),
                    currentItem.streamingURI.toString()
                )
                it.findNavController().navigate(action)
            }
            deviceEdit.setOnClickListener {
                val directionUpdateDevice =
                    DeviceFragmentDirections.actionDeviceToInfoDeviceFragment(
                        currentItem.id.toString(),
                        currentItem.name.toString(),
                        currentItem.clientID.toString(),
                        currentItem.token.toString(),
                        currentItem.streamingURI.toString(),
                        currentItem.description.toString()
                    )
                it.findNavController().navigate(directionUpdateDevice)
            }
        }
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    /*

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