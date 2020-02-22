package com.example.myapplication.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Checkpoint
import com.example.myapplication.MainActivity
import com.example.myapplication.R

class RecyclerViewAdapter(
    val context: Context
) : RecyclerView.Adapter<RecyclerViewViewHolder>() {

    private val list: List<Checkpoint>
        get() {
            return MainActivity.path
        }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        val item = list.get(position)
        holder.textView.setText("%.2f, %.2f".format(item.coordinate.x, item.coordinate.y))
        if (item.checked) {
            holder.imageView.setImageDrawable(context.resources.getDrawable(R.drawable.full_checkpoint))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        return RecyclerViewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false))
    }

}