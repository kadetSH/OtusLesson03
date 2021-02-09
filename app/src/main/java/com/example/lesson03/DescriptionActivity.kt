package com.example.lesson03

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DescriptionActivity : AppCompatActivity() {

    var nameFilm: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.description_layout)

        val name = intent.getStringExtra("nameFilm")

        //Проверяем наличие названия фильма и ищем описание по номеру массива
        name?.let {
            nameFilm = it
            var titleArray: Array<String> = resources.getStringArray(R.array.film)
            var nn = titleArray.indexOf(it)
            if (nn >= 0) {
                var description = resources.getStringArray(R.array.film_description)[nn]
                findViewById<TextView>(R.id.descriptionId).setText(description)
            }
        }

        findViewById<ImageView>(R.id.imageFilmId).setImageResource(
            intent.getIntExtra(
                "imageId",
                R.drawable.ic_launcher_foreground
            )
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("nameOfDescription", nameFilm)
            var value : String = ""

            if (findViewById<CheckBox>(R.id.likeId).isChecked) {
                value = "Нравится"
            } else {
                value = "Не нравится"
            }
            putExtra("like", value)

            val comment = findViewById<TextView>(R.id.comentId).text.toString()
            putExtra("comment", comment)
        }
        startActivity(intent)
    }
}