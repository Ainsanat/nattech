package com.example.azimuth

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardViewHolder(view: View): RecyclerView.ViewHolder(view) {
    var textName: TextView = view.findViewById(R.id.tv_name_device)
    var textID: TextView = view.findViewById(R.id.deviceID)
}