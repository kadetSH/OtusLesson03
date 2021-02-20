package com.example.lesson03

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lesson03.recyclerMy.FilmsAdapter
import com.example.lesson03.recyclerMy.FilmsItem


class FilmsDescriptionFragment : Fragment() {

    companion object {

        fun newInstance(list: FilmsItem): FilmsDescriptionFragment {
            val args = Bundle()
            args.putSerializable("spisok", list)

            val fragment = FilmsDescriptionFragment()
            fragment.arguments = args
            return fragment
        }


        var adapter: FilmsAdapter? = null
    }


    var list: FilmsItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list = arguments?.getSerializable("spisok") as FilmsItem
        println("")
        var spisokFilm = resources.getStringArray(R.array.film)
        val imageId = view.findViewById<ImageView>(R.id.app_bar_image)
        val descriptionId = view.findViewById<TextView>(R.id.description)
        list?.let {
            imageId.setImageResource(it.imageFilm)
            var pos = spisokFilm.indexOf(it.nameFilm)
            if (pos > -1) {
                descriptionId.text = resources.getStringArray(R.array.film_description)[pos]
            } else {
                descriptionId.text = it.shortDescription
            }
        }


    }
}