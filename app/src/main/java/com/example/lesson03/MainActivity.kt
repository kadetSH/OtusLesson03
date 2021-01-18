package com.example.lesson03



import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    var adapter : MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("Фильмы")

        var list = ArrayList<SpisokItem>()
        //массивы по фильмам отправляем в функцию для заполнения массива шаблонов
        list.addAll(fillArrays(resources.getStringArray(R.array.film), getImageId(R.array.film_image), resources.getStringArray(R.array.film_description)))

        findViewById<RecyclerView>(R.id.spisok).hasFixedSize()
        findViewById<RecyclerView>(R.id.spisok).layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(list, this)
        findViewById<RecyclerView>(R.id.spisok).adapter = adapter
    }

    fun fillArrays(titleArray:Array<String>, filmImageArray : IntArray, descriptionArray : Array<String>) : List<SpisokItem>{
        var list = ArrayList<SpisokItem>()
        for (i in 0..titleArray.size-1){
            var shortDescription = descriptionArray[i]
            var spisokItem = SpisokItem(titleArray[i], filmImageArray[i], shortDescription.substring(0, 120) + "...")
            list.add(spisokItem)
        }
        return list
    }

    //Расшифровка идентификаторов картинок. Получаем количество фильмов, циклом перебираем картинки и получаем их id
    fun getImageId(filmImageArrayId : Int) : IntArray{
        var iArray : TypedArray = resources.obtainTypedArray(filmImageArrayId)
        val count = iArray.length()
        val ids = IntArray(count)
        for (i in ids.indices){
            ids[i] = iArray.getResourceId(i,0)
        }
        iArray.recycle()
        return ids
    }
}