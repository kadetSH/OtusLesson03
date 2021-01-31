package com.example.lesson03

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Annotation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColor
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.animation.AnimationUtils.DECELERATE_INTERPOLATOR
import kotlinx.android.synthetic.main.activity_favorites.*


class MyAdapter(spisokArray: ArrayList<SpisokItem>, context: Context, private val clickListener : (spisokItem : SpisokItem, position: Int) -> Unit) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    var spisokArray = spisokArray
    var context = context


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nameFilm = view.findViewById<TextView>(R.id.nameFilm)
        val imageFilm = view.findViewById<ImageView>(R.id.imageFilmId)
        val shortDescription = view.findViewById<TextView>(R.id.shortDescription)
        val description = view.findViewById<Button>(R.id.description)
        val share = view.findViewById<Button>(R.id.share)
        var star = view.findViewById<ImageView>(R.id.idStar)
//        val fullSpisok = view.findViewById<RecyclerView>(R.id.spisok)






        fun bind(spisokItem: SpisokItem, context: Context) {

            nameFilm.text = spisokItem.nameFilm
            if (nameFilm.text == spisokItem.proverka) {
                nameFilm.setTextColor(Color.GREEN)
            }
            imageFilm.setImageResource(spisokItem.imageFilm)
            shortDescription.text = spisokItem.shortDescription
            star.isSelected = spisokItem.star

            val colorTrue = ContextCompat.getColor(context, R.color.starTrue)
            val colorFalse = ContextCompat.getColor(context, R.color.starFalse)
//            star.isSelected
            if (star.isSelected == true){
                var starAnim = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.scale_star)
                star.startAnimation(starAnim)
                star.setBackgroundColor(colorTrue)
            }else star.setBackgroundColor(colorFalse)


            description.setOnClickListener { it ->
//                Toast.makeText(context, "нажали", Toast.LENGTH_SHORT).show()
                nameFilm.setTextColor(Color.GREEN)
                val intent = Intent(context, DescriptionActivity::class.java).apply {
                    putExtra("nameFilm", nameFilm.text.toString())
                    putExtra("imageId", spisokItem.imageFilm)
                }
                context.startActivity(intent)
            }

            share.setOnClickListener {
//                val intent = Intent(context, SelectActivity::class.java)
//                context.startActivity(intent)
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:")) //, Uri.parse("mailto:")
//                context.startActivity(intent)
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "textMessage")
                    type = "text/plain"
                }
                context.startActivity(sendIntent)
            }

            star.setOnClickListener {
//                Toast.makeText(context, "нажали", Toast.LENGTH_SHORT).show()
                var starAnim = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.scale_star)

                star.startAnimation(starAnim)

                var colorDraw = star.background?.mutate()
                colorDraw?.let {
                    var intColorDraw = (it as ColorDrawable).color
                    if (intColorDraw == colorFalse) star.setBackgroundColor(colorTrue)
                    else star.setBackgroundColor(colorFalse)
                }
                if (colorDraw == null){
                    star.setBackgroundColor(colorTrue)
                }

//
//                val rr = fullSpisok?.getChildAdapterPosition(it)
//
//                println("")
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

//        holder.itemView.setOnClickListener {
//            clickListener(spisokArray[position])
//        }

        holder.star.setOnClickListener {
            clickListener(spisokArray[position], position)
        }


    }

    override fun getItemCount(): Int {
        return spisokArray.size
    }





}