package com.example.nattech

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardViewHolder(view: View): RecyclerView.ViewHolder(view) {
    var textName = view.findViewById<TextView>(R.id.tv_name_machine)
    var textMonthYear = view.findViewById<TextView>(R.id.tv_month_year)
    var textDate = view.findViewById<TextView>(R.id.tv_date)
    var imageTrash = view.findViewById<ImageView>(R.id.imgTrash)
}