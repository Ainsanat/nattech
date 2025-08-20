package com.example.azimuth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CoordinateAdapter(private val itemCoordinateList: List<CoordinateItem>): RecyclerView.Adapter<CoordinateAdapter.ViewHolder>() {
    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        val itemTitle: TextView = itemView.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coordinate_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemCoordinateList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemCoordinateList[position]
        holder.itemImage.setImageResource(currentItem.imageLocation)
        holder.itemTitle.text = currentItem.title
    }
}