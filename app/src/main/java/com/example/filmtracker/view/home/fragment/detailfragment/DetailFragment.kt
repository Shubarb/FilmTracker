package com.example.filmtracker.view.home.fragment.detailfragment

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmtracker.R
import com.example.filmtracker.databinding.FragmentDetailBinding
import com.example.filmtracker.models.CastAndCrew
import com.example.filmtracker.models.Constants
import com.example.filmtracker.models.Movie
import com.example.filmtracker.network.Resource
import com.example.filmtracker.view.home.fragment.MovieViewModel
import com.example.filmtracker.view.home.fragment.MovieViewModelFactory
import com.squareup.picasso.Picasso

class DetailFragment(

    ) : Fragment(),View.OnClickListener {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var mMovie: Movie
    private lateinit var mCastAndCrewList: ArrayList<CastAndCrew>
    private lateinit var mCastAndCrewAdapter: CastAndCrewAdapter

    private var mReminderExisted : Boolean = false
    private lateinit var mMovieReminder : Movie

    private var mSaveDay= 0
    private var mSaveMonth= 0
    private var mSaveYear= 0
    private var mSaveHour= 0
    private var mSaveMinute= 0

    private lateinit var mHandler: Handler

    private val noteViewModel: MovieViewModel by lazy {
        ViewModelProvider(this, MovieViewModelFactory(requireActivity().getApplication())).get(
            MovieViewModel::class.java
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val bundle = this.arguments
        if(bundle != null){
            mMovie = bundle.getSerializable("movieDetail")as Movie
//            val movieReminderList = mDataBaseOpenHelper.getReminderMovieID(mMovie.id)
//            if(movieReminderList.isEmpty()){
//                mReminderExisted = false
//            }else{
//                mReminderExisted = true
//                mMovieReminder = movieReminderList[0]
//            }
        }

        if(mMovie.isFavorite){
            binding.btnFavoriteDetail.setImageResource(R.drawable.ic_baseline_star_rate_24)
        }else
            binding.btnFavoriteDetail.setImageResource(R.drawable.ic_baseline_star_outline_24)
        binding.btnFavoriteDetail.setOnClickListener(this)

        binding.txtreleasedetail.text = mMovie.releaseDate
        "${mMovie.voteAverage}/10".also { binding.txtratedetail.text = it }
        val url = Constants.BASE_IMG_URL + mMovie.posterPath
        Picasso.get().load(url).into(binding.imgposterdetail)
        binding.txtoverviewdetail.text = mMovie.overview

        initObserve()
        mCastAndCrewList = arrayListOf()
        initView()
        mHandler = Handler(Looper.getMainLooper())
        mHandler.postDelayed({
            getCastAndCrewFromApi()
        }, 1000)

        return rootView
    }

    private fun initView(){
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mCastAndCrewAdapter = CastAndCrewAdapter(requireActivity(),mCastAndCrewList)
        binding.rcvCastDetail.layoutManager = layoutManager
        binding.rcvCastDetail.setHasFixedSize(true)
        binding.rcvCastDetail.adapter = mCastAndCrewAdapter
    }

    private fun initObserve(){
        // Arraylist from call api
        noteViewModel.stateListDetail.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.e("log", "... Loading ...")
                }
                Resource.Status.FAILED -> {

                    Toast.makeText(requireContext(), "Error:${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                Resource.Status.SUCCESS -> {
                    val listCast = it.data!!.castList
                    val listCrew = it.data!!.crewList// List<Movie>
                    mCastAndCrewList.addAll(listCast)
                    mCastAndCrewList.addAll(listCrew)
                    mCastAndCrewAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun getCastAndCrewFromApi() {
        noteViewModel.getCastAndCrew(mMovie.id!!, Constants.API_KEY)
    }

    override fun onClick(p0: View?) {

    }

}