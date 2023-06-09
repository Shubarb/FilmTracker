package com.example.filmtracker.view.home.fragment.homefragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmtracker.R
import com.example.filmtracker.database.DataBaseOpenHelper
import com.example.filmtracker.database.MovieDatabase
import com.example.filmtracker.databinding.FragmentHomeBinding
import com.example.filmtracker.models.Constant
import com.example.filmtracker.models.Constants
import com.example.filmtracker.models.Movie
import com.example.filmtracker.network.*

class HomeFragment(
    private var mScreenType: Int
) : Fragment(),View.OnClickListener  {

    private lateinit var mDatabaseOpenHelper: DataBaseOpenHelper
    private lateinit var mMovieList: ArrayList<Movie>
    private var mIsGridview: Boolean = false
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mGridLayoutManager: GridLayoutManager
    private var mViewType: Int = HomeAdapter.TYPE_LIST
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mHomeAdapter: HomeAdapter
    private lateinit var homeViewModel : HomeViewModel
    private lateinit var mApiSerVice: ApiService
    private lateinit var mApiRepo: ApiRepo
    private lateinit var mApiDatasource: ApiDataSource
    private lateinit var mDatabase: MovieDatabase
    private lateinit var mMovieListType: String
    private var mPage = 0
    private var isLoadMore: Boolean = false
    private var isRefresh: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val rootView = binding.root
        val sharedPreferencesDefault = PreferenceManager.getDefaultSharedPreferences(requireContext())
        mMovieListType =
            sharedPreferencesDefault.getString(Constant.PREF_CATEGORY_KEY, "upcoming").toString()
        val convertType: String = when (mMovieListType) {
            "Top rated movies" -> "top_rate"
            "Up coming movies" -> "upcoming"
            else -> "now_playing"
        }
        mApiSerVice = GetRetrofit().getInstance().create(ApiService::class.java)
        mApiDatasource = ApiDataSource(mApiSerVice)
        mApiRepo = ApiRepo(mApiDatasource)
        mDatabase = MovieDatabase.getInstance(requireContext())
        val factory = HomeViewModelFactory(mApiRepo, mDatabase)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        mDatabaseOpenHelper = DataBaseOpenHelper(requireContext(), "movie_database", null, 1)

        mMovieList = ArrayList()
        mHomeAdapter = HomeAdapter(mMovieList,mViewType,this,false)
        mLinearLayoutManager = LinearLayoutManager(activity)
        mGridLayoutManager = GridLayoutManager(activity,2)

        initView()
        initObserve()

        SearchClickListener()
        TypeViewCLickListener()

        if(isLoadMore){
            mPage += 1
        }else{
            if(!isRefresh){
                binding.progressBar.visibility = View.VISIBLE
            }
        }
        homeViewModel.getAllMovie(convertType, Constants.API_KEY,"$mPage")
        homeViewModel.getAllData()
        return rootView
    }

    private fun initObserve() {
        homeViewModel.stateListMovie.observe(requireActivity()){
            when(it.status){
                Resource.Status.LOADING -> {
                    Log.e("log", "... Loading ...")
                }
                Resource.Status.FAILED -> {
                    Toast.makeText(requireContext(), "Error:${it.message}", Toast.LENGTH_SHORT).show()
                }
                Resource.Status.SUCCESS -> {
                    val listMovie = it.data!!.result // ArrayList<Character>
                    mMovieList.addAll(listMovie)
                    mHomeAdapter.notifyDataSetChanged()
                }
            }
        }
        homeViewModel.listState.observe(requireActivity()){
//            binding.txtTitle.text = "Size: ${it.size}"
        }
    }

    private fun initView() {
        if(mScreenType == 0){
            binding.listRecyclerview.layoutManager = mLinearLayoutManager
        }else{
            binding.listRecyclerview.layoutManager = mGridLayoutManager
        }
        binding.listRecyclerview.setHasFixedSize(true)
        binding.listRecyclerview.adapter = mHomeAdapter
    }

    private fun TypeViewCLickListener() {
        binding.tyoeView.setOnClickListener {
            mIsGridview = !mIsGridview
            if (mIsGridview) {
                binding.tyoeView.setImageResource(R.drawable.list)
            } else {
                binding.tyoeView.setImageResource(R.drawable.menu)
            }
            changeViewHome()
        }
    }

    private fun changeViewHome() {
        binding.edtSearch.visibility = View.GONE
        binding.searchMovie.setBackgroundResource(0)
        binding.imgSearch.setBackgroundResource(R.drawable.bg_blur)
        if(binding.listRecyclerview.layoutManager == mGridLayoutManager){
            mViewType = HomeAdapter.TYPE_LIST
            binding.listRecyclerview.layoutManager = mLinearLayoutManager

        }else{
            mViewType = HomeAdapter.TYPE_GRID
            binding.listRecyclerview.layoutManager = mGridLayoutManager
        }
        mHomeAdapter.setViewType(mViewType)
        binding.listRecyclerview.adapter = mHomeAdapter
        mHomeAdapter.notifyDataSetChanged()
    }

    private fun SearchClickListener() {
        binding.searchMovie.setOnClickListener {
            binding.searchMovie.setBackgroundResource(R.drawable.bg_blur)
            binding.edtSearch.visibility = View.VISIBLE
            binding.imgSearch.setBackgroundResource(0)
        }
    }

    override fun onClick(p0: View?) {

    }


}