package com.example.lesson03



import android.content.res.TypedArray
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.*
import java.util.*


class MainActivity : AppCompatActivity()  {

    var adapter : MyAdapter? = null
    var filmP : String = ""
    private val TAG = "myLogs"
    private val nameFilmPole by lazy {
        findViewById<TextView>(R.id.nameFilm)
    }

    val spisokFull by lazy {
        findViewById<RecyclerView>(R.id.spisok)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("Фильмы")

        //Получаем названия фильма после возвращения с описания
        val name  = intent.getStringExtra("nameOfDescription")
        name?.let {
            filmP = it
        }

//        val comment  = intent.getStringExtra("comment")
//        name?.let {
//            filmP = it
//        }

        val like = intent.getStringExtra("like")
        like?.let {
            Log.d(TAG, like)
        }
        val comment = intent.getStringExtra("comment")
        comment?.let {
            Log.d(TAG, comment)
        }



        var list = ArrayList<SpisokItem>()
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
        adapter = MyAdapter(list, this)
        spisokFull.adapter = adapter

    }

    fun fillArrays(
        titleArray: Array<String>,
        filmImageArray: IntArray,
        descriptionArray: Array<String>
    ) : List<SpisokItem>{
        var list = ArrayList<SpisokItem>()
        for (i in 0..titleArray.size-1){
            var shortDescription = descriptionArray[i]
            var proverka = ""
            if (titleArray[i].equals(filmP)){
                proverka = filmP
            }

            var spisokItem = SpisokItem(
                titleArray[i], filmImageArray[i], shortDescription.substring(
                    0,
                    120
                ) + "...", proverka
            )
            list.add(spisokItem)
        }
        return list
    }

    //Расшифровка идентификаторов картинок. Получаем количество фильмов, циклом перебираем картинки и получаем их id
    fun getImageId(filmImageArrayId: Int) : IntArray{
        var iArray : TypedArray = resources.obtainTypedArray(filmImageArrayId)
        val count = iArray.length()
        val ids = IntArray(count)
        for (i in ids.indices){
            ids[i] = iArray.getResourceId(i, 0)
        }
        iArray.recycle()
        return ids
    }



}