package com.example.myapplication.recyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_item.view.*

class RecyclerViewViewHolder (itemView: View
): RecyclerView.ViewHolder(itemView) {


    var imageView: ImageView = itemView.recyclerIcon
    var textView: TextView = itemView.recyclerText

}