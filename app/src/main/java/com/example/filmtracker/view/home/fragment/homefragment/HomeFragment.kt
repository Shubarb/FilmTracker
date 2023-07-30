package com.example.filmtracker.view.home.fragment.homefragment

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
import com.example.filmtracker.database.MovieDatabase
import com.example.filmtracker.databinding.FragmentHomeBinding
import com.example.filmtracker.models.Constant
import com.example.filmtracker.models.Constants
import com.example.filmtracker.models.Movie
import com.example.filmtracker.network.*
import com.example.filmtracker.view.home.fragment.BadgeListener
import com.example.filmtracker.view.home.fragment.detailfragment.DetailListener
import com.example.filmtracker.view.home.fragment.favoritefragment.MovieViewModel
import com.example.filmtracker.view.home.fragment.favoritefragment.MovieViewModelFactory

class HomeFragment(
    private var mScreenType: Int,
//    private var mDatabaseOpenHelper: DataBaseOpenHelper
) : Fragment(),View.OnClickListener  {

//    private lateinit var mDatabaseOpenHelper: DataBaseOpenHelper
    private lateinit var mMovieList: ArrayList<Movie>
    private var mIsGridview: Boolean = false
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mGridLayoutManager: GridLayoutManager
    private var mViewType: Int = HomeAdapter.TYPE_LIST
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mHomeAdapter: HomeAdapter
    private lateinit var mApiSerVice: ApiService
    private lateinit var mApiRepo: ApiRepo
    private lateinit var mHandler: Handler
    private lateinit var mApiDatasource: ApiDataSource
    private lateinit var mDatabase: MovieDatabase
    private lateinit var mMovieListType: String
    private var mPage = 1
    private lateinit var convertType: String
    private val noteViewModel: MovieViewModel by lazy {
        ViewModelProvider(this, MovieViewModelFactory(requireActivity().getApplication())).get(MovieViewModel::class.java)
    }
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this, HomeViewModelFactory(requireActivity().getApplication())).get(HomeViewModel::class.java)
    }

//    fun updateMovieList(movie: Movie,isFavorite:Boolean){
//        mMovieList[findById(movie.id!!)].isFavorite = isFavorite
//        mHomeAdapter.notifyDataSetChanged()
//    }

//    fun setReminderListener(reminderListener: ReminderListener){
//        this.mReminderListener = reminderListener
//    }

//    fun setListMovieByCondition(){
//        loadDataBySetting()
//        updateMovieList()
//        mHandler.postDelayed({
//            getListMovieFromApi(false,false)
//        },1000)
//
//    }

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
        convertType = when (mMovieListType) {
            "Top rated movies" -> "top_rate"
            "Up coming movies" -> "upcoming"
            else -> "now_playing"
        }
//        mApiSerVice = GetRetrofit().getInstance().create(ApiService::class.java)
//        mApiDatasource = ApiDataSource(mApiSerVice)
////        mApiRepo = ApiRepo(mApiDatasource,)
//        mDatabase = MovieDatabase.getInstance(requireContext())
        mHandler = Handler(Looper.getMainLooper())
        mMovieList = ArrayList()
        mHomeAdapter = HomeAdapter(mMovieList,mViewType,this,onClickitem,false)
        mLinearLayoutManager = LinearLayoutManager(activity)
        mGridLayoutManager = GridLayoutManager(activity,2)


        initView()
        initObserve()

        SearchClickListener()
        TypeViewCLickListener()

        mHandler.postDelayed({
            getListMovieFromApi(false,false)
        },1000)

        binding.swipeLayout.setOnRefreshListener {
            mHandler.postDelayed({
                initView()
                getListMovieFromApi(true,false)
            },1000)
        }

        loadMore()
        return rootView
    }


    private fun getListMovieFromApi(isRefresh: Boolean,isLoadMore: Boolean){
        mHomeAdapter.removeItemLoading()
        if(isLoadMore){
            mPage += 1
        }else{
            if(!isRefresh){
                binding.progressBar.visibility = View.VISIBLE
            }
        }
        homeViewModel.getAllMovie(convertType, Constants.API_KEY,"$mPage")
        if(!isLoadMore && !isRefresh){
            binding.progressBar.visibility = View.GONE
        }
        if(isRefresh){
            binding.swipeLayout.isRefreshing = false
        }
//        mHomeAdapter.setupMovieFavorite(mMovieList)
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
                    val listMovie = it.data!!.result // ArrayList<Movie>
                    mMovieList.addAll(listMovie)
                    for (i in listMovie){
                        homeViewModel.insertList(i)
                    }
                    if(mPage< it.data.totalPages){
                        val loadMoreItem =
                            Movie(0,"0","0",0.0,"0","0",false,false,"0","0",false)
                        mMovieList.add(loadMoreItem)
                    }
                    mHomeAdapter.notifyDataSetChanged()


                }
            }
        }

    }

    private fun initView() {
        mPage = 1
        if(mScreenType == 1){
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

    private fun findById(id:Int):Int{
        var position = -1
        val size = mMovieList.size
        for(i in 0 until size){
            if(mMovieList[i].id == id){
                position = i
                break
            }
        }
        return position
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

    private fun isLastItemDisplaying(recyclerView: RecyclerView): Boolean{
        if(recyclerView.adapter!!.itemCount !=0){
            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager)!!.findLastCompletelyVisibleItemPosition()
            if(lastVisibleItemPosition!=RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.adapter!!
                    .itemCount -1
            )return true
        }
        return false
    }

    private fun loadMore(){
        binding.listRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(isLastItemDisplaying(recyclerView)){
                    mHandler.postDelayed({
                        getListMovieFromApi(false,true)
                    },1000)
                }
            }
        })
    }

    private val onClickitem: (Movie)-> Unit = {
        if(it.isFavorite!!){
            noteViewModel.deleteNote(it)
            Toast.makeText(requireContext(),"delete ${it.title}",Toast.LENGTH_SHORT).show()
            it.isFavorite = false
        }else{
            noteViewModel.insertNote(it)
            Toast.makeText(requireContext(),"insert ${it.title}",Toast.LENGTH_SHORT).show()
            it.isFavorite = true
        }
        mHomeAdapter.notifyDataSetChanged()
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.movie_item -> {
//                val position = p0.tag as Int
//                val movieDetail: Movie = mMovieList[position]
//                val bundle= Bundle()
//                bundle.putSerializable("movieDetail",movieDetail)
//                val detailFragment = DetailFragment(mDatabaseOpenHelper)
//                detailFragment.setDetailListener(mDetailListener)
//                detailFragment.setBadgeListener(mBadgeListener)
//                detailFragment.setRemindListener(mReminderListener)
//                detailFragment.arguments = bundle
//                mMovieDetailFragment.arguments = bundle
//                requireActivity().supportFragmentManager.beginTransaction().apply {
//                    add(R.id.frg_home,detailFragment,Constant.FRAGMENT_DETAIL_TAG)
//                    addToBackStack(null)
//                    commit()
//                    mHomeListener.onUpdateTitleMovie(movieDetail.title,true)
//                }
            }
        }
    }


}