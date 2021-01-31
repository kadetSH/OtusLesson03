package com.example.lesson03


import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.template.view.*


class MainActivity : AppCompatActivity() {

    var adapter: MyAdapter? = null
    var filmP: String = ""
    private val TAG = "myLogs"
//    private val nameFilmPole by lazy {
//        findViewById<TextView>(R.id.nameFilm)
//    }

    val spisokFull by lazy {
        findViewById<RecyclerView>(R.id.spisok)
    }

    val star by lazy {
        findViewById<ImageView>(R.id.idStar)
    }

    val butAdd by lazy {
        findViewById<Button>(R.id.butAdd)
    }

    val butDell by lazy {
        findViewById<Button>(R.id.butDell)
    }

    val butFavorites by lazy {
        findViewById<Button>(R.id.butFavorites)
    }
    var starSpisok: ArrayList<String> = ArrayList()
    var starSpisokPosition: ArrayList<Int> = ArrayList()
    var list = ArrayList<SpisokItem>()
    var colorTrue: Int? = null
    var colorFalse: Int? = null
    var favoriteName : ArrayList<String> = ArrayList()


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_films)
        colorFalse = ContextCompat.getColor(baseContext, R.color.starFalse)
        colorTrue = ContextCompat.getColor(baseContext, R.color.starTrue)

        setTitle("Фильмы")

        //Получаем названия фильма после возвращения с описания
        val name = intent.getStringExtra("nameOfDescription")
        name?.let {
            filmP = it
        }/////////

        //Получаем названия фильмов после возвращения из фаворитов
        val getfavoriteName   = intent.getStringArrayListExtra("favoriteList")
        getfavoriteName?.let {
            favoriteName = it
        }


        val like = intent.getStringExtra("like")
        like?.let {
            Log.d(TAG, like)
        }
        val comment = intent.getStringExtra("comment")
        comment?.let {
            Log.d(TAG, comment)
        }

        //Выводим список любимых фильмов
        butFavorites.setOnClickListener {
            favoritesOnClick()
        }
        ////////////////////////////

        //Добавить вручную фильм
        butAdd.setOnClickListener {
            clickAddFilm()
        }
        /////////////////////////

        //Удалить вручную фильм
        butDell.setOnClickListener {
            clickDellFilm()
        }
        /////////////////////////


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

        findViewById<RecyclerView>(R.id.spisok).clearFindViewByIdCache()
        findViewById<RecyclerView>(R.id.spisok).hasFixedSize()
        findViewById<RecyclerView>(R.id.spisok).layoutManager = LinearLayoutManager(this)
        initRecycler()
//        adapter = MyAdapter(list, this) { spisokItem: SpisokItem, position: Int ->
//            selectFavorites(spisokItem, position)
//        }
//        spisokFull.adapter = adapter


    }

    private fun clickAddFilm() {

        var count = list.size

        var spisokItem = SpisokItem(
            "Добавили фильм $count",
            R.drawable.ic_launcher_foreground,
            "Описание добавленного фильма $count",
            "",
            false)
        list.add(spisokItem)
        spisokFull.adapter?.notifyItemInserted(count)

    }

    private fun clickDellFilm() {

        var count = list.size-1

        list.removeAt(count)
        spisokFull.adapter?.notifyItemRemoved(count)

    }


    fun fillArrays(
        titleArray: Array<String>,
        filmImageArray: IntArray,
        descriptionArray: Array<String>,
    ): List<SpisokItem> {
        var list = ArrayList<SpisokItem>()
        for (i in 0..titleArray.size - 1) {
            var shortDescription = descriptionArray[i]
            var proverka = ""
            if (titleArray[i].equals(filmP)) {
                proverka = filmP
            }
            var idxFav = favoriteName.indexOf(titleArray[i])
            var boolFavorite : Boolean

            if (idxFav == -1) {
                boolFavorite = false
            }else{
                boolFavorite = true
            }
            var spisokItem = SpisokItem(
                titleArray[i], filmImageArray[i], shortDescription.substring(
                    0,
                    120
                ) + "...", proverka, boolFavorite
            )
            list.add(spisokItem)
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

    fun selectFavorites(spisokItem: SpisokItem, position: Int) {

        var proverka: Boolean = false

        if ((spisokItem.star == false) && (proverka == false)) {
            proverka = true
            spisokItem.star = true
            starSpisok.add(spisokItem.nameFilm)
            starSpisokPosition.add(position)
//            colorStar(spisokItem, position)
            adapter?.notifyItemChanged(position)
        }
        if ((spisokItem.star == true) && (proverka == false)) {
            proverka = true
            spisokItem.star = false
            var indexName = starSpisok.indexOf(spisokItem.nameFilm)
            starSpisok.removeAt(indexName)
            var indexPosition = starSpisokPosition.indexOf(position)
            starSpisokPosition.removeAt(indexPosition)
//            colorStar(spisokItem, position)
            adapter?.notifyItemChanged(position)
        }

    }

    fun colorStar(spisokItem: SpisokItem, position: Int) {
        var count = spisokFull.size
        var selectItem = spisokFull.getChildAt(position).idStar
        var colorDraw = selectItem?.background?.mutate()


//        var selectItem = spisokFull.get(position).idStar
//        var colorDraw = selectItem.idStar.background?.mutate()
        colorDraw?.let {
            var intColorDraw = (it as ColorDrawable).color
            if (intColorDraw == colorFalse) colorTrue?.let { it1 ->
                selectItem.setBackgroundColor(it1)
            }
            if ((intColorDraw == colorTrue)) colorFalse?.let { it1 ->
                selectItem.setBackgroundColor(it1)
            }
        }
        if (colorDraw == null) {
            colorTrue?.let { star.setBackgroundColor(it) }
        }
    }

    fun favoritesOnClick() {

        if ((starSpisok.size == 0) && (favoriteName.size == 0)){
            Toast.makeText(baseContext, "Нет помеченных фильмов", Toast.LENGTH_SHORT).show()
            }
        else{
            var spisokFilm = resources.getStringArray(R.array.film)

            for (favorName in favoriteName){
                    var namPos = spisokFilm.indexOf(favorName)
                    if (namPos > -1){
                        var proverka = starSpisokPosition.indexOf(namPos)
                        if (proverka == -1){
                            starSpisokPosition.add(namPos)
                        }
                    }
            }
            val intent = Intent(baseContext, FavoritesActivity::class.java).apply {
                putExtra("starSpisokPosition", starSpisokPosition)
            }
            baseContext.startActivity(intent)
        }

    }

    fun initRecycler() {
//        val layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        adapter = MyAdapter(list, this) { spisokItem: SpisokItem, position: Int ->
            selectFavorites(spisokItem, position)
        }

        spisokFull.addItemDecoration(Decor(22))
        spisokFull.adapter = adapter

        
    }


}