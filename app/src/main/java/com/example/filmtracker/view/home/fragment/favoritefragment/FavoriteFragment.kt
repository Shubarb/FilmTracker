package com.example.filmtracker.view.home.fragment.favoritefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmtracker.databinding.FragmentFavoriteBinding
import com.example.filmtracker.models.Movie

class FavoriteFragment(
) : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var mMovieAdapter: FavoriteAdapter
    private var listMovie : ArrayList<Movie> = arrayListOf()


    private val movieViewModel: MovieViewModel by lazy {
        ViewModelProvider(this, MovieViewModelFactory(requireActivity().getApplication())).get(MovieViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModel.getAllNote()
        loadFavoriteList()
        setHasOptionsMenu(true)
    }

    private fun loadFavoriteList(){
        mMovieAdapter = FavoriteAdapter(requireActivity(),onDeleteItem)
        binding.listRecyclerview.layoutManager = LinearLayoutManager(requireActivity())
        binding.listRecyclerview.setHasFixedSize(true)
        binding.listRecyclerview.adapter = mMovieAdapter

        movieViewModel.movieState.observe(requireActivity()){
            mMovieAdapter.setNotes(it)
            mMovieAdapter.notifyDataSetChanged()
            listMovie.clear()
            listMovie.addAll(it)
        }
    }

    private val onDeleteItem: (Movie) -> Unit = {
        movieViewModel.deleteNote(it)
        listMovie.remove(it)
        mMovieAdapter.setNotes(listMovie)
        Toast.makeText(requireContext(),"delete ${it.title}", Toast.LENGTH_SHORT).show()
    }

}