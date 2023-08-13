package com.example.filmtracker.view.home.fragment.homefragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmtracker.R
import com.example.filmtracker.databinding.MovieItemBinding
import com.example.filmtracker.databinding.MovieItemGridBinding
import com.example.filmtracker.databinding.MovieItemLoadBinding
import com.example.filmtracker.databinding.MovieItemLoadGridBinding
import com.example.filmtracker.models.CastAndCrew
import com.example.filmtracker.models.Constants
import com.example.filmtracker.models.Movie
import com.squareup.picasso.Picasso

class HomeAdapter(
    private var mlistMovie: MutableList<Movie>,
    private var mViewType: Int,
    private var mViewClickListener: View.OnClickListener,
    private val onClickFavorite: (Movie) -> Unit,
    private val onClick: (Movie)-> Unit,
    private var mIsFavoriteList: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_LIST = 0
        const val TYPE_GRID = 1
        const val TYPE_LOADING_LIST = 2
        const val TYPE_LOADING_GRID = 3
    }

    fun updateData(listMovie: MutableList<Movie>) {
        this.mlistMovie = listMovie
        notifyDataSetChanged()
    }

    fun setViewType(viewType: Int) {
        this.mViewType = viewType
    }

    fun removeItemLoading() {
        if (mlistMovie.isNotEmpty()) {
            val lastPosition = mlistMovie.size - 1
            mlistMovie.removeAt(lastPosition)
            notifyDataSetChanged()
        }
    }

    fun setupMovieBySetting(
        listMovie: ArrayList<Movie>,
        rate: Int,
        releaseYear: String,
        sortBy: String
    ) {

        listMovie.removeAll { it.voteAverage!! < rate }

        val convertYear: Int? = if (releaseYear.length > 3) {
            releaseYear.substring(0, 4).trim().toIntOrNull()
        } else {
            null
        }

        if (convertYear != null) {
            listMovie.removeAll {
                it.releaseDate!!.substring(0, 4).trim() != releaseYear
            }
        }

//        if (sortBy == "Release Date")
//            listMovie.sortByDescending { it.releaseDate }
//        else if (sortBy == "Rating")
//            listMovie.sortByDescending { it.voteAverage }
//        else if (sortBy == "None")
//            null

        Log.e("checkData", "origin list: ${this.mlistMovie.size}")
        Log.e("checkData", "list setting: ${listMovie.size}")
        updateData(listMovie)
        Log.e("checkData", "new list: ${this.mlistMovie.size}")
    }
    override fun getItemCount(): Int {

        return mlistMovie.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (!mIsFavoriteList && mlistMovie.isNotEmpty() && position == mlistMovie.size - 1 && mViewType == TYPE_LIST) {
            TYPE_LOADING_LIST
        } else if (!mIsFavoriteList && mlistMovie.isNotEmpty() && position == mlistMovie.size - 1 && mViewType == TYPE_GRID) {
            TYPE_LOADING_GRID
        } else {
            mViewType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_LIST) {
            ListViewHolder(
                 mlistMovie,
                MovieItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            )
        } else if (viewType == TYPE_GRID) {
            GridViewHolder(
                mlistMovie,
                MovieItemGridBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            )
        } else if (viewType == TYPE_LOADING_LIST) {
            LoadListViewHolder(
                MovieItemLoadBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            )
        } else {
            LoadGridViewHolder(
                MovieItemLoadGridBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tag = position
        holder.itemView.setOnClickListener(mViewClickListener)
        if (holder is GridViewHolder) {
            holder.bindata(position)
        } else if (holder is ListViewHolder) {
            holder.bindData(position)
        }
    }

    class GridViewHolder(
        private var movieList: MutableList<Movie>,
        private var binding: MovieItemGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindata(position: Int) {
            val movie = movieList[position]
            val url = Constants.BASE_IMG_URL + movie.posterPath
            Picasso.get().load(url).into(binding.imgMovieGrid)
            binding.tvTitleGrid.text = movie.title
        }
    }

    inner class ListViewHolder(
        private var movieList: MutableList<Movie>,
        private var binding: MovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {
            val movie = movieList[position]
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
            if (movie.isFavorite) {
                binding.imgBtn.setImageResource(R.drawable.ic_baseline_star_rate_24)
            } else {
                binding.imgBtn.setImageResource(R.drawable.ic_baseline_star_outline_24)
            }
            binding.imgBtn.tag = position
            binding.imgBtn.setOnClickListener{
                movie.isFavorite != movie.isFavorite
                onClickFavorite(movie)
            }
            binding.movieitem.setOnClickListener {
                onClick(movie)
            }
        }
    }

    class LoadListViewHolder(private var binding: MovieItemLoadBinding) : RecyclerView.ViewHolder(binding.root)

    class LoadGridViewHolder(private var binding: MovieItemLoadGridBinding) : RecyclerView.ViewHolder(binding.root)


}