package com.example.lesson03

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.description_layout)

        val name  = intent.getStringExtra("nameFilm")

        //Проверяем наличие названия фильма и ищем описание по номеру массива
        name.let {
            var titleArray:Array<String> = resources.getStringArray(R.array.film)
            var nn =titleArray.indexOf(it)
            if (nn >=0){
                var description = resources.getStringArray(R.array.film_description)[nn]
                findViewById<TextView>(R.id.descriptionId).setText(description)
            }
        }

        findViewById<ImageView>(R.id.imageFilmId).setImageResource(intent.getIntExtra("imageId", R.drawable.ic_launcher_foreground))
    }
}