package com.example.filmtracker.view.home.fragment.detailfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmtracker.R
import com.example.filmtracker.databinding.CastCrewItemBinding
import com.example.filmtracker.databinding.MovieItemBinding
import com.example.filmtracker.models.CastAndCrew
import com.example.filmtracker.models.Constants
import com.squareup.picasso.Picasso

class CastAndCrewAdapter (
    private val context: Context,
    private var mCastAndCrewList: ArrayList<CastAndCrew>
): RecyclerView.Adapter<CastAndCrewAdapter.CastAndCrewViewHolder>(){

    override fun getItemCount(): Int {
        return mCastAndCrewList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastAndCrewViewHolder {
        return CastAndCrewViewHolder(
            CastCrewItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: CastAndCrewViewHolder, position: Int){
        holder.bindDataCastAndCrew(mCastAndCrewList[position])
    }

    class CastAndCrewViewHolder(private var binding: CastCrewItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bindDataCastAndCrew(castAndCrew: CastAndCrew){
            val url = Constants.BASE_IMG_URL + castAndCrew.profilePath
            Picasso.get().load(url).error(R.drawable.shubarb).into(binding.castCrewItemAvatarImage)
            binding.castCrewItemNameText.text = castAndCrew.name
        }
    }

}