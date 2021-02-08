package com.example.lesson03

import android.content.DialogInterface
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.template.view.*

class FavoritesActivity : AppCompatActivity() {

    var adapter: MyAdapter? = null
    var starSpisokPosition: ArrayList<Int>? = null
    var colorFalse: Int? = null
    var list = ArrayList<SpisokItem>()
    val spisokFullFavorites by lazy {
        findViewById<RecyclerView>(R.id.favoritesSpisok)
    }
    val star by lazy {
        findViewById<ImageView>(R.id.idStar)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        setTitle(resources.getString(R.string.title_favorite))

        starSpisokPosition = intent.getIntegerArrayListExtra("starSpisokPosition") as ArrayList<Int>
        colorFalse = ContextCompat.getColor(baseContext, R.color.starFalse)

        //массивы по фильмам отправляем в функцию для заполнения массива шаблонов
        list.addAll(
            fillArrays(
                resources.getStringArray(R.array.film),
                getImageId(R.array.film_image),
                resources.getStringArray(
                    R.array.film_description
                )
            )
        )
        initRecycler(list)
    }


    fun fillArrays(
        titleArray: Array<String>,
        filmImageArray: IntArray,
        descriptionArray: Array<String>,
    ): List<SpisokItem> {
        var list = ArrayList<SpisokItem>()

        starSpisokPosition?.let {
            for (pos in it) {
                var shortDescription = descriptionArray[pos]
                var proverka = ""

                var spisokItem = SpisokItem(
                    titleArray[pos], filmImageArray[pos], shortDescription.substring(
                        0,
                        120
                    ) + "...", proverka, true
                )
                list.add(spisokItem)
            }
        }
        return list
    }

    //Расшифровка идентификаторов картинок. Получаем количество фильмов, циклом перебираем картинки и получаем их id
    fun getImageId(filmImageArrayId: Int): IntArray {
        var iArray: TypedArray = resources.obtainTypedArray(filmImageArrayId)
        val count = iArray.length()
        val ids = IntArray(count)
        for (i in ids.indices) {
            ids[i] = iArray.getResourceId(i, 0)
        }
        iArray.recycle()
        return ids
    }

    fun initRecycler(list: ArrayList<SpisokItem>) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        favoritesSpisok.layoutManager = layoutManager

        adapter = MyAdapter(list, this) { spisokItem: SpisokItem, position: Int ->
            colorStarDellFavorite(position)
        }
        favoritesSpisok.adapter = adapter
    }

    fun colorStarDellFavorite(position: Int) {
        list.removeAt(position)
        spisokFullFavorites.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        exitDialog(spisokFullFavorites)
//        currentFocus?.let { exitDialog(it) }
    }

    fun exitDialog(view : View){
        val alertD : AlertDialog.Builder = AlertDialog.Builder(this)
        val listenerCancel = DialogInterface.OnClickListener { dialog, which ->
            Toast.makeText(this, "Нет", Toast.LENGTH_LONG).show()
        }
        val listenerExit = DialogInterface.OnClickListener { dialog, which ->
//            Toast.makeText(this, "Да", Toast.LENGTH_LONG).show()
                    var favoriteName : ArrayList<String> = ArrayList()

        for (name in list){
            favoriteName.add(name.nameFilm)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("favoriteList", favoriteName)
        }
        this.startActivity(intent)
        }
        alertD.setMessage("Хотите вернуться в общий список фильмов?")
        alertD.setNegativeButton("Нет", listenerCancel)
        alertD.setPositiveButton("Да", listenerExit)
        val dialog : AlertDialog = alertD.create()
        dialog.show()
    }
}