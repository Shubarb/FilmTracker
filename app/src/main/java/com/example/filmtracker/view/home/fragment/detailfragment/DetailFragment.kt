package com.example.filmtracker.view.home.fragment.detailfragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.example.filmtracker.utility.NotificationUtil
import com.example.filmtracker.view.home.fragment.MovieViewModel
import com.example.filmtracker.view.home.fragment.MovieViewModelFactory
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class DetailFragment: Fragment(){

    private lateinit var binding: FragmentDetailBinding
    private lateinit var mMovie: Movie
    private var mCastAndCrewList: ArrayList<CastAndCrew> = arrayListOf()
    private var listRemind: ArrayList<Movie> = arrayListOf()
    private lateinit var mCastAndCrewAdapter: CastAndCrewAdapter

    private var mReminderExisted : Boolean = false
    private var mMovieReminder : Movie? = null


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

        noteViewModel.getAllRemind()
        val bundle = this.arguments
        if(bundle != null){
            mMovie = bundle.getSerializable("movieDetail")as Movie
            Log.e("movie","Movie receive: ${mMovie!!.reminderTimeDisplay}")
        }
        initObserve()

        favoriteDetail()
        remindDetail()
        initView()

        mHandler = Handler(Looper.getMainLooper())
        mHandler.postDelayed({
            getCastAndCrewFromApi()
        }, 1000)


        return rootView
    }

    private fun remindDetail() {
        binding.btnReminderDetail.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                createReminder()
            }
        }
    }

    private fun favoriteDetail() {
        Toast.makeText(requireActivity(),"Back To Home Film Tracker To Remove Star",Toast.LENGTH_SHORT).show()
    }

    private fun initView(){
        if(mMovie.isFavorite){
            binding.btnFavoriteDetail.setImageResource(R.drawable.ic_baseline_star_rate_24)
        }else
            binding.btnFavoriteDetail.setImageResource(R.drawable.ic_baseline_star_outline_24)
        binding.txtreleasedetail.text = mMovie.releaseDate
        "${mMovie.voteAverage}/10".also { binding.txtratedetail.text = it }
        val url = Constants.BASE_IMG_URL + mMovie.posterPath
        Picasso.get().load(url).into(binding.imgposterdetail)
        binding.txtoverviewdetail.text = mMovie.overview

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

        // ArrayList Remind from room
        noteViewModel.remindState.observe(requireActivity()){
            listRemind.clear()
            listRemind.addAll(it)
            val isCheck: Boolean = listRemind.any{ it.id == mMovie.id}
            if(isCheck){
                mReminderExisted = true
                mMovieReminder = listRemind.firstOrNull { it.id == mMovie.id }
                if(mReminderExisted){
                    binding.reminderTimeText.visibility = View.VISIBLE
                    binding.reminderTimeText.text = mMovieReminder!!.reminderTimeDisplay
                }else{
                    binding.reminderTimeText.visibility = View.GONE
                }
                Log.e("movie","Movie from list remind: ${mMovieReminder!!.reminderTimeDisplay}")
            }else{
                mReminderExisted = false
                Log.e("movie","Movie isn't exist")
            }
        }
    }

    private fun getCastAndCrewFromApi() {
        noteViewModel.getCastAndCrew(mMovie.id!!, Constants.API_KEY)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createReminder(){
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(),{ _,year,month,day ->
            TimePickerDialog(requireContext(),{_,hour,minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year,month,day,hour,minute)
                mSaveYear =year
                mSaveMonth = month
                mSaveDay = day
                mSaveHour = hour
                mSaveMinute = minute
                currentDateTime.set(mSaveYear,mSaveMonth,mSaveDay,mSaveHour,mSaveMinute)
                val reminderTimeInMillis: Long = currentDateTime.timeInMillis
                val reminderTimeDisplay = "$mSaveYear/${mSaveMonth+1}/$mSaveDay  $mSaveHour:$mSaveMinute"
                binding.reminderTimeText.visibility = View.VISIBLE
                binding.reminderTimeText.text = reminderTimeDisplay

                mMovie.reminderTimeDisplay = reminderTimeDisplay
                mMovie.reminderTime = reminderTimeInMillis.toString()

                if(mReminderExisted){
                    noteViewModel.removeRemind(mMovie)
                    noteViewModel.insertRemind(mMovie)
                    Log.e("lpa","Movie Star after remind: ${mMovie.isFavorite} ")
                    NotificationUtil().cancelNotification(mMovie.id?:0,requireContext())
                    NotificationUtil().createNotification(
                        mMovie,
                        reminderTimeInMillis,
                        requireContext()
                    )
                    Toast.makeText(context,"update",Toast.LENGTH_SHORT).show()

                }else{
                    noteViewModel.insertRemind(mMovie)
                    Log.e("lpa","Movie Star after remind: ${mMovie.isFavorite} ")
                    NotificationUtil().createNotification(
                        mMovie,
                        reminderTimeInMillis,
                        requireContext()
                    )
                    Toast.makeText(context,"Create new",Toast.LENGTH_SHORT).show()

                }
            },startHour,startMinute,true).show()
        },startYear,startMonth,startDay).show()
    }

}