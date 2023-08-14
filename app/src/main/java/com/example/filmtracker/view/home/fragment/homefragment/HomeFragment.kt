package com.example.filmtracker.view.home.fragment.homefragment

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmtracker.R
import com.example.filmtracker.databinding.FragmentHomeBinding
import com.example.filmtracker.models.Constant
import com.example.filmtracker.models.Constants
import com.example.filmtracker.models.Movie
import com.example.filmtracker.network.Resource
import com.example.filmtracker.view.home.fragment.MovieViewModel
import com.example.filmtracker.view.home.fragment.MovieViewModelFactory
import com.example.filmtracker.view.home.fragment.detailfragment.DetailFragment
import com.example.filmtracker.view.home.fragment.settingfragment.SettingFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class HomeFragment(
    private var mScreenType: Int
) : Fragment(), View.OnClickListener {

    private lateinit var mMovieList: ArrayList<Movie>
    private lateinit var mMovieListFavorite: ArrayList<Movie>

    private var mIsGridview: Boolean = false
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mGridLayoutManager: GridLayoutManager
    private var mViewType: Int = HomeAdapter.TYPE_LIST
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mHomeAdapter: HomeAdapter
    private lateinit var mHandler: Handler

    private lateinit var mMovieListType: String
    private var mPage = 1
    private var mRatePref: Int = 0
    private lateinit var mReleaseYearPref: String
    private lateinit var mSortByPref: String
    private lateinit var convertType: String

    private val noteViewModel: MovieViewModel by lazy {
        ViewModelProvider(this, MovieViewModelFactory(requireActivity().getApplication())).get(
            MovieViewModel::class.java
        )
    }
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("kiemtra","Create Home Frag")
//        EventBus.getDefault().register(this)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val rootView = binding.root
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        loadDataBySetting()
        val sharedPreferencesDefault =
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        mMovieListType =
            sharedPreferencesDefault.getString(Constant.PREF_CATEGORY_KEY, "upcoming").toString()
        convertType = when (mMovieListType) {
            "Popular Movies" -> "popular"
            "Top Rated Movies" -> "top_rate"
            "Up Coming Movies" -> "upcoming"
            else -> "now_playing"
        }

        mHandler = Handler(Looper.getMainLooper())
        mMovieList = ArrayList()
        mMovieListFavorite = ArrayList()

        initObserve()

        mHomeAdapter =
            HomeAdapter(mMovieList, mViewType, this, onClickIconFavorite, onClickMovie, false)
        mLinearLayoutManager = LinearLayoutManager(activity)
        mGridLayoutManager = GridLayoutManager(activity, 2)
        mHandler.postDelayed({
            getListMovieFromApi(false, false)
        }, 1000)



        binding.swipeLayout.setOnRefreshListener {
            mHandler.postDelayed({
                initView()
                getListMovieFromApi(true, false)
            }, 1000)
        }
        noteViewModel.getAllFavorite()
        initView()
        SearchClickListener()
        TypeViewCLickListener()
        SettingClickListener()
        loadMore()
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("kiemtra", "Destroy Home Frag")
        EventBus.getDefault().unregister(this)
    }

    override fun onPause() {
        super.onPause()
        Log.e("kiemtra", "Pause Home Frag")
    }

    override fun onStop() {
        super.onStop()
        Log.e("kiemtra", "Stop Home Frag")
    }

    private fun getListMovieFromApi(isRefresh: Boolean, isLoadMore: Boolean) {
        mHomeAdapter.removeItemLoading()
        if (isLoadMore) {
            mPage += 1
        } else {
            if (!isRefresh) {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
        noteViewModel.getAllMovie(convertType, Constants.API_KEY, "$mPage")
        if (!isLoadMore && !isRefresh) {
            binding.progressBar.visibility = View.GONE
        }
        if (isRefresh) {
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun initObserve() {
        // Arraylist from Favorite
        noteViewModel.movieState.observe(requireActivity()) {
            var list = it
            mMovieListFavorite.addAll(list)
            Log.e("ww", "Size film favo: ${mMovieListFavorite.size}")
        }

        // Arraylist from call api
        noteViewModel.stateListMovie.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.e("log", "... Loading ...")
                }
                Resource.Status.FAILED -> {

                    Toast.makeText(requireContext(), "Error:${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                Resource.Status.SUCCESS -> {
                    val listMovie = it.data!!.result // List<Movie>
                    mMovieList.addAll(listMovie)
                    Log.e("ww", "Size film call add list ${mMovieList.size}")
                    if (mPage < it.data.totalPages) {
                        val loadMoreItem =
                            Movie(0, "0", "0", 0.0, "0", "0", false, false, "0", "0", false)
                        mMovieList.add(loadMoreItem)
                    }
//                    mHomeAdapter.notifyDataSetChanged()
                    // check favourite movie
                    for (i in mMovieListFavorite) {
                        val same = mMovieList.find { it.id == i.id }
                        if (same != null && !i.isFavorite) {
                            same.isFavorite = true
                            mHomeAdapter.updateData(mMovieList)
                            Toast.makeText(requireContext(), "Update list", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    mHomeAdapter.setupMovieBySetting(
                        mMovieList,
                        mRatePref,
                        mReleaseYearPref,
                        mSortByPref
                    )
                    mHomeAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    private fun initView() {
        mPage = 1
        if (mScreenType == 1) {
            binding.listRecyclerview.layoutManager = mLinearLayoutManager
        } else {
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

    private fun SettingClickListener() {
        binding.iconSetting.setOnClickListener {
            val settingFragment = SettingFragment()
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.viewPager, settingFragment, Constant.FRAGMENT_DETAIL_TAG)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun loadDataBySetting() {
        convertType =
            mSharedPreferences.getString(Constant.PREF_CATEGORY_KEY, "upcoming").toString()
        mRatePref = mSharedPreferences.getInt(Constant.PREF_RATE_KEY, 0)
        mReleaseYearPref = mSharedPreferences.getString(Constant.PREF_RELEASE_KEY, "").toString()
        mSortByPref = mSharedPreferences.getString(Constant.PREF_SORT_KEY, "").toString()
    }

    private fun changeViewHome() {
        binding.edtSearch.visibility = View.GONE
        binding.searchMovie.setBackgroundResource(0)
        binding.imgSearch.setBackgroundResource(R.drawable.bg_blur)
        if (binding.listRecyclerview.layoutManager == mGridLayoutManager) {
            mViewType = HomeAdapter.TYPE_LIST
            binding.listRecyclerview.layoutManager = mLinearLayoutManager

        } else {
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

    private fun isLastItemDisplaying(recyclerView: RecyclerView): Boolean {
        if (recyclerView.adapter!!.itemCount != 0) {
            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager)!!.findLastCompletelyVisibleItemPosition()
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.adapter!!
                    .itemCount - 1
            ) return true
        }
        return false
    }

    private fun loadMore() {
        binding.listRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isLastItemDisplaying(recyclerView)) {
                    mHandler.postDelayed({
                        getListMovieFromApi(false, true)
                    }, 1000)
                }
            }
        })
    }

    private val onClickIconFavorite: (Movie) -> Unit = {
        if (it.isFavorite!!) {
            noteViewModel.deleteFavorite(it)
            Toast.makeText(requireContext(), "delete ${it.title}", Toast.LENGTH_SHORT).show()
            it.isFavorite = false
        } else {
            noteViewModel.insertFavorite(it)
            Toast.makeText(requireContext(), "insert ${it.title}", Toast.LENGTH_SHORT).show()
            it.isFavorite = true
        }
        mHomeAdapter.notifyDataSetChanged()
    }

    private val onClickMovie: (Movie) -> Unit = {
        val bundle = Bundle()
        bundle.putSerializable("movieDetail", it)
        val detailFragment = DetailFragment()
        detailFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.viewPager, detailFragment, Constant.FRAGMENT_DETAIL_TAG)
            addToBackStack(null)
            commit()
        }
    }

    override fun onClick(p0: View) {
    }

    private var isUpdate = false

    override fun onResume() {
        super.onResume()
        initObserve()
        Log.e("kiemtra","Resume Home frag")
        Log.e("checkData", "isUpdate: $isUpdate")
    }

//
//    @Subscribe
//    fun onEventUpcate(event: SettingFragment.UpdateListMovie) {
//        isUpdate = event.isUpdate
//    }

}