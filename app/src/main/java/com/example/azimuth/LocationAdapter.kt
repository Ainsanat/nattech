package com.example.azimuth

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class LocationAdapter(private val locationList: List<LocationItem>) :
    RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
    private var lastAnimatedPosition = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val positionNumber: TextView = itemView.findViewById(R.id.position_number)
        val latLong: TextView = itemView.findViewById(R.id.position_latlng)
        val locationItem: CardView = itemView.findViewById(R.id.position_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.position_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentItem = locationList[position]
        val locationItemNumber = position + 1
        val location = "Position $locationItemNumber"
        holder.positionNumber.text = location
        holder.latLong.text = currentItem.latLng.toString()

        if (position > lastAnimatedPosition) {
            val animation = AnimationUtils.loadAnimation(holder.locationItem.context, R.anim.scale_in_animation)
            holder.itemView.startAnimation(animation)
            lastAnimatedPosition = position
        }
    }
    fun getAllItems(): List<LocationItem>{
        return locationList
    }
}