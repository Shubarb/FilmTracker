package com.example.filmtracker.view.home.fragment.favoritefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmtracker.databinding.FragmentFavoriteBinding
import com.example.filmtracker.models.Movie
import com.example.filmtracker.view.home.fragment.BadgeListener

class FavoriteFragment(
) : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
//    private lateinit var mMovieAdapter: FavoriteAdapter
    private lateinit var mBadgeListener: BadgeListener
    private lateinit var mHomeFavoriteListener: FavoriteListener
    private val movieViewModel: MovieViewModel by lazy {
        ViewModelProvider(this, MovieViewModelFactory(requireActivity().getApplication())).get(MovieViewModel::class.java)
    }
    fun setBadgeListener(badgeListener: BadgeListener){
        mBadgeListener = badgeListener
        Log.e("eee","call")
    }

    fun setFavoriteListener(favoriteListener: FavoriteListener){
        mHomeFavoriteListener = favoriteListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val rootView = binding.root

        movieViewModel.getAllNote()
        loadFavoriteList()
        setHasOptionsMenu(true)
        return rootView
    }

    private fun loadFavoriteList(){
        val mMovieAdapter = FavoriteAdapter(requireActivity(),onDeleteItem)
        binding.listRecyclerview.layoutManager = LinearLayoutManager(requireActivity())
        binding.listRecyclerview.setHasFixedSize(true)
        binding.listRecyclerview.adapter = mMovieAdapter
//        movieViewModel.getAllNote().observe(requireActivity(), Observer {
//            mMovieAdapter.setNotes(it)
//            val ii = it.size
//            Toast.makeText(requireContext(),"$ii",Toast.LENGTH_SHORT).show()
//        })
        movieViewModel.movieState.observe(requireActivity()){
            mMovieAdapter.setNotes(it)
        }
    }

    private val onDeleteItem: (Movie) -> Unit = {
        movieViewModel.deleteNote(it)
        Toast.makeText(requireContext(),"delete ${it.title}", Toast.LENGTH_SHORT).show()
    }

}