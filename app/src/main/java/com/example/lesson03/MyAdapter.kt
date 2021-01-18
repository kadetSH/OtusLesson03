package com.example.lesson03

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(spisokArray : ArrayList<SpisokItem>, context: Context) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    var spisokArray = spisokArray
    var context = context

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val nameFilm = view.findViewById<TextView>(R.id.nameFilm)
        val imageFilm = view.findViewById<ImageView>(R.id.imageFilmId)
        val shortDescription = view.findViewById<TextView>(R.id.shortDescription)
        val description = view.findViewById<Button>(R.id.description)
        val share = view.findViewById<Button>(R.id.share)

        fun bind(spisokItem: SpisokItem, context: Context){

            nameFilm.text = spisokItem.nameFilm
            imageFilm.setImageResource(spisokItem.imageFilm)
            shortDescription.text = spisokItem.shortDescription
            description.setOnClickListener { it ->
                Toast.makeText(context, "Описание", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, DescriptionActivity::class.java).apply {
                    putExtra("nameFilm", nameFilm.text.toString())
                    putExtra("imageId", spisokItem.imageFilm)
                }
                context.startActivity(intent)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.template, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var listItem = spisokArray.get(position)
        holder.bind(listItem, context)
    }

    override fun getItemCount(): Int {
        return spisokArray.size
    }
}