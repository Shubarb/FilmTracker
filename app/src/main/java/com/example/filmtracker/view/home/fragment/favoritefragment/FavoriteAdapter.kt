package com.example.filmtracker.view.home.fragment.favoritefragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmtracker.R
import com.example.filmtracker.databinding.MovieItemBinding
import com.example.filmtracker.models.Constants
import com.example.filmtracker.models.Movie
import com.squareup.picasso.Picasso

class FavoriteAdapter(
    private val context: Context,
    private val onDelete: (Movie) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.MovieViewHolder>() {

    private var note: List<Movie> = listOf()

    inner class MovieViewHolder(
//        private var movieList: List<Movie>,
        private var binding: MovieItemBinding
        ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(movie: Movie,position: Int) {
//            val movie = note[position]
            binding.tvTitle.text = movie.title
            val urlImage = Constants.BASE_IMG_URL + movie.posterPath
            Picasso.get().load(urlImage).into(binding.imgMovie)
            binding.tvDate.text = movie.releaseDate
            "${movie.voteAverage}/10".also { binding.tvRate.text = it }
            if (movie.adult!!) {
                binding.imgAdult.visibility = View.VISIBLE
            } else {
                binding.imgAdult.visibility = View.GONE
            }
            binding.tvOverview.text = movie.overview
//            if (movie.isFavorite!!) {
                binding.imgBtn.setImageResource(R.drawable.ic_baseline_star_rate_24)
//            } else {
//                binding.imgBtn.setImageResource(R.drawable.ic_baseline_star_outline_24)
//            }
            binding.imgBtn.tag = position
            binding.imgBtn.setOnClickListener{
                onDelete(movie)
                binding.imgBtn.setImageResource(R.drawable.ic_baseline_star_outline_24)
            }
        }
    }

    override fun getItemCount(): Int = note.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            MovieItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.onBind(note[position],position)
    }

    fun setNotes(notes: List<Movie>) {
        this.note = notes
        notifyDataSetChanged()
    }
}