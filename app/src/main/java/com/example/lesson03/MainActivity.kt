package com.example.lesson03

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.lesson03.recyclerMy.FilmsItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    FilmsFragment.OnFilmLikeClickListener {

    var list = ArrayList<FilmsItem>()
    var filmP: String = ""
    var favoriteName: ArrayList<String> = ArrayList()
    var starSpisok = ArrayList<FilmsItem>()
    var starSpisokPosition: ArrayList<Int> = ArrayList()
    val fab by lazy {
        findViewById<FloatingActionButton>(R.id.fab)
    }
    var snackbar : Snackbar? = null
    var selectItem: FilmsItem? = null
    var selectItemAct : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.addAll(
            fillArrays(
                resources.getStringArray(R.array.film),
                getImageId(R.array.film_image),
                resources.getStringArray(
                    R.array.film_description
                )
            )
        )

        id_navigation.setNavigationItemSelectedListener(this)
        fab.setOnClickListener {
            fabOnClickListener(it)
        }

        openFilmsList()
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_films -> openFilmsList()
            R.id.id_favorites -> openFavorites()
            R.id.id_films_add -> addFilm()
            R.id.id_films_dell -> dellFilm()
            R.id.id_invite -> Toast.makeText(this, "Нажали пригласить", Toast.LENGTH_SHORT).show()
        }
        id_drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }else super.finish()
    }

    private fun fabOnClickListener(view: View){
        val snackbar = Snackbar.make(view, "Our Snackbar", Snackbar.LENGTH_INDEFINITE)
        snackbar.show()

        view.postDelayed({
            snackbar.dismiss()
        }, 5000)
    }


    private fun openFilmsList() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, FilmsFragment.newInstance(list))
            .commit()
    }

    private fun openFavorites() {

        if (starSpisok.size == 0) {
            Toast.makeText(baseContext, "Нет помеченных фильмов", Toast.LENGTH_SHORT).show()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, FilmsFragment.newInstance(starSpisok))
                .addToBackStack(null)
                .commit()
        }
    }

    private fun openDescriptions(spisokItem: FilmsItem, position: Int) {

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, FilmsDescriptionFragment.newInstance(spisokItem))
            .addToBackStack(null)
            .commit()
    }

    private fun addFilm() {
        var count = list.size
        var spisokItem = FilmsItem(
            "Добавили фильм $count",
            R.drawable.ic_launcher_foreground,
            "Описание добавленного фильма $count",
            "",
            false
        )
        list.add(spisokItem)
        FilmsFragment.adapter?.notifyItemInserted(count)
    }

    private fun dellFilm() {
        var count = list.size - 1
        list.removeAt(count)
        FilmsFragment.adapter?.notifyItemRemoved(count)
    }

    fun selectFavorites(spisokItem: FilmsItem, position: Int) {
        selectItem = spisokItem
        selectItemAct = "добавили"
        var proverka: Boolean = false
        if ((spisokItem.star == false) && (proverka == false)) {

            proverka = true
            spisokItem.star = true
            starSpisok.add(list[position])
            FilmsFragment.adapter?.notifyItemChanged(position)
//            Toast.makeText(this, "${spisokItem.nameFilm} добавили в избранное", Toast.LENGTH_SHORT)
//                .show()
            snackbarShow(spisokItem.nameFilm, "добавили")
        }
        if ((spisokItem.star == true) && (proverka == false)) {
            selectItem = spisokItem
            selectItemAct = "удалили"
            proverka = true
            spisokItem.star = false
//            Toast.makeText(this, "${spisokItem.nameFilm} удалили из избранного", Toast.LENGTH_SHORT)
//                .show()
            var pos = starSpisok.indexOf(spisokItem)
            if (pos > -1) {
                starSpisok.removeAt(pos)
            }
            FilmsFragment.adapter?.notifyDataSetChanged() //notifyItemChanged(position)
            snackbarShow("${spisokItem.nameFilm}", "удалили")
        }
    }

    fun snackbarShow(name : String, act : String){
        val listener = View.OnClickListener {
            println("")
            if (selectItemAct.equals("добавили")){
                selectItem?.star = false
                var pos = starSpisok.indexOf(selectItem)
                if (pos > -1) {
                    starSpisok.removeAt(pos)
                }
                FilmsFragment.adapter?.notifyDataSetChanged()
            }else{
                selectItem?.star = true
                selectItem?.let { it1 -> starSpisok.add(it1) }
                FilmsFragment.adapter?.notifyDataSetChanged()
            }

        }
        if (act.equals("добавили")) snackbar = Snackbar.make(fab, "$name - добавили в избранное", Snackbar.LENGTH_INDEFINITE)
        if (act.equals("удалили")) snackbar = Snackbar.make(fab, "$name - удалили из избранного", Snackbar.LENGTH_INDEFINITE)
        snackbar?.setAction("Отменить", listener )
        snackbar?.show()
        fab.postDelayed({
            snackbar?.dismiss()
        }, 3000)
    }

    fun fillArrays(
        titleArray: Array<String>,
        filmImageArray: IntArray,
        descriptionArray: Array<String>,
    ): List<FilmsItem> {
        var list = ArrayList<FilmsItem>()
        for (i in 0..titleArray.size - 1) {
            var shortDescription = descriptionArray[i]
            var proverka = ""
            if (titleArray[i].equals(filmP)) {
                proverka = filmP
            }
            var idxFav = favoriteName.indexOf(titleArray[i])
            var boolFavorite: Boolean

            if (idxFav == -1) {
                boolFavorite = false
            } else {
                boolFavorite = true
            }
            var spisokItem = FilmsItem(
                titleArray[i], filmImageArray[i], shortDescription.substring(
                    0,
                    120
                ) + "...", proverka, boolFavorite
            )
            list.add(spisokItem)
        }
        return list
    }

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


    override fun onFilmLikeClick(filmsItem: FilmsItem, position: Int, note: String) {
        if (note.equals("star")) {
            selectFavorites(filmsItem, position)
        } else if (note.equals("description")) {
            openDescriptions(filmsItem, position)
        }
    }

}