package com.example.azimuth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

internal class GridAdapter(
    private val courseList: List<GridViewModel>,
    private val context: Context
    ): BaseAdapter(){
    private var layoutInflater: LayoutInflater? = null
    private lateinit var courseTV: TextView
    private lateinit var courseIndicator: TextView
    private lateinit var courseIV: ImageView

    override fun getCount(): Int {
        return courseList.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {
        var convertView = p1
        if (layoutInflater == null){
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null){
            convertView = layoutInflater!!.inflate(R.layout.gridview_item, null)
        }
        courseIV = convertView!!.findViewById(R.id.imagev)
        courseIndicator = convertView!!.findViewById(R.id.tv_indicator)
        courseTV = convertView!!.findViewById(R.id.tv_description)

        courseIV.setImageResource(courseList.get(p0).icon)
        courseIndicator.setText(courseList.get(p0).indicator)
        courseTV.setText(courseList.get(p0).name)

        return convertView
    }

}