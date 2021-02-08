package com.example.lesson03

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson03.recyclerMy.FilmsAdapter
import com.example.lesson03.recyclerMy.FilmsItem


class FilmsFragment : Fragment() {

    companion object {

        fun newInstance(list: ArrayList<FilmsItem>): FilmsFragment {
            val args = Bundle()
            args.putSerializable("spisok", list)

            val fragment = FilmsFragment()
            fragment.arguments = args
            return fragment
        }


        var adapter: FilmsAdapter? = null
    }

    var list = ArrayList<FilmsItem>()
    var filmP: String = ""
    var favoriteName: ArrayList<String> = ArrayList()






    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_films_list, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        list.addAll(
//            fillArrays(
//                resources.getStringArray(R.array.film),
//                getImageId(R.array.film_image),
//                resources.getStringArray(
//                    R.array.film_description
//                )
//            )
//        )

        list = arguments?.getSerializable("spisok") as ArrayList<FilmsItem>

        adapter = FilmsAdapter(
            LayoutInflater.from(requireContext()),
            list
        ) { filmsItem: FilmsItem, position: Int, note: String ->
//            selectFavorites(filmsItem, position)
            (activity as? OnFilmLikeClickListener)?.onFilmLikeClick(filmsItem, position, note)
        }

        view.findViewById<RecyclerView>(R.id.id_recyclerView)
            .adapter = adapter
    }

    interface OnFilmLikeClickListener{
        fun onFilmLikeClick(filmsItem: FilmsItem, position: Int, note : String)
    }


}