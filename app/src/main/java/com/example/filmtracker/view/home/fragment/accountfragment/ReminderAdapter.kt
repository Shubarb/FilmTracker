package com.example.filmtracker.view.home.fragment.accountfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmtracker.databinding.ReminderItemBinding
import com.example.filmtracker.models.Constants
import com.example.filmtracker.models.Movie
import com.squareup.picasso.Picasso

class ReminderAdapter(
    private val context: Context,
    private var type: Int,
    private val onClick: (Movie) -> Unit,
    private val onLongClick: (Movie) -> Unit
) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>(){

    private var listMovieReminder: List<Movie> = listOf()
    companion object{
        const val REMINDER_ALL = 1
        const val REMINDER_PROFILE = 0
    }

    fun updateData(listMovieReminder: List<Movie>){
        this.listMovieReminder = listMovieReminder
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        return ReminderViewHolder(
                type,
                ReminderItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bindData(position,listMovieReminder[position])
    }

    override fun getItemCount(): Int {
        return if(type == REMINDER_ALL){
            listMovieReminder.size
        }else{
            if(listMovieReminder.size >3) 3 else listMovieReminder.size
        }
    }

    inner class ReminderViewHolder(
        private var reminderType: Int,
        private var binding: ReminderItemBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bindData(position:Int,movie:Movie){
            if(reminderType == REMINDER_PROFILE){
                binding.posterImgRemind.visibility = View.GONE
            }else{
                binding.posterImgRemind.visibility = View.VISIBLE
                val url = Constants.BASE_IMG_URL + movie.posterPath
                Picasso.get().load(url).into(binding.posterImgRemind)
                itemView.setOnClickListener{
                    onClick(movie)
                }
                itemView.setOnLongClickListener{
                    onLongClick(movie)
                    true
                }
            }

            binding.txtTitleRemind.text = movie.title
            "${movie.releaseDate}".also { binding.txtReleaseRemind.text = it }
            binding.txtDateRemind.text = movie.reminderTimeDisplay
        }
    }



}