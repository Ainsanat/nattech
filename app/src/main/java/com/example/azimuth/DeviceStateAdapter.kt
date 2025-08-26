package com.example.azimuth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeviceStateAdapter(private val stateDeviceList: List<StateDeviceItem>): RecyclerView.Adapter<DeviceStateAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dName: TextView = itemView.findViewById(R.id.status_device_name)
        val dBattery: TextView = itemView.findViewById(R.id.status_battery)
        val dVelocity: TextView = itemView.findViewById(R.id.status_velocity)
        val dTemp: TextView = itemView.findViewById(R.id.status_temp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_state_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stateDeviceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = stateDeviceList[position]
        holder.apply {
            dName.text = currentItem.nameDevice
            dBattery.text = currentItem.battery
            dVelocity.text = currentItem.velocity
            dTemp.text = currentItem.temperature
        }
    }
}