package com.example.filmtracker.view.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.filmtracker.R
import com.example.filmtracker.databinding.ActivityHomeBinding
import com.example.filmtracker.models.Movie
import com.example.filmtracker.view.home.fragment.BadgeListener
import com.example.filmtracker.view.home.fragment.accountfragment.AccountFragment
import com.example.filmtracker.view.home.fragment.detailfragment.DetailListener
import com.example.filmtracker.view.home.fragment.favoritefragment.FavoriteFragment
import com.example.filmtracker.view.home.fragment.favoritefragment.FavoriteListener
import com.example.filmtracker.view.home.fragment.favoritefragment.MovieViewModel
import com.example.filmtracker.view.home.fragment.favoritefragment.MovieViewModelFactory
import com.example.filmtracker.view.home.fragment.homefragment.HomeFragment
import com.example.filmtracker.view.home.fragment.homefragment.HomeListener
import com.example.filmtracker.view.home.fragment.messagefragment.MessageFragment
import com.example.filmtracker.view.home.fragment.settingfragment.SettingFragment

class Home : AppCompatActivity()
//    ,HomeListener,BadgeListener,FavoriteListener,DetailListener
{

    lateinit var binding: ActivityHomeBinding
    private lateinit var mDrawerLayout: DrawerLayout

    private lateinit var mHomeFragment: HomeFragment
    private lateinit var mfavoriteFragment: FavoriteFragment
    private lateinit var mMessageFragment: MessageFragment
    private lateinit var mAccountFragment: AccountFragment
    private lateinit var mSettingFragment: SettingFragment

    private lateinit var mMovieFavoriteList: List<Movie>
//    private var mDatabaseOpenHelper: DataBaseOpenHelper? = null
    private var ID_Home = 1
    private var ID_Favorite = 2
    private var ID_Message = 3
    private var ID_Account = 4
    private var name = ""
//    private var count = 0
    var count: Int = 0

    private var mFavoriteCount: Int = 0
//    private var bottomNavigation: MeowBottomNavigation? = null
    private val movieViewModel: MovieViewModel by lazy {
        ViewModelProvider(this, MovieViewModelFactory(this.application)).get(
            MovieViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

//        mDatabaseOpenHelper = DataBaseOpenHelper(this, "movie_database", null, 1)
//        mMovieFavoriteList = mDatabaseOpenHelper!!.getListMovie()
        //Fragment view
        mHomeFragment = HomeFragment(1)
        mfavoriteFragment = FavoriteFragment()
        mMessageFragment = MessageFragment()
        mSettingFragment = SettingFragment()
        mAccountFragment = AccountFragment()
        //callback fragment
//        mHomeFragment.setBadgeListener(this)
//        mHomeFragment.setHomeListener(this)
//        mHomeFragment.setDetailListener(this)
//        mHomeFragment.setReminderListener(this)

//        mfavoriteFragment.setBadgeListener(this)
//        mfavoriteFragment.setFavoriteListener(this)


//        mMovieFavoriteList = mDatabaseOpenHelper!!.getListMovie()
        movieViewModel.countLiveData.observe(this){
            count = it

            Log.e("ww", "thread: ${Thread.currentThread().name}")
            runOnUiThread {
                Log.e("ww", "thread: ${Thread.currentThread().name}")
                Log.e("ww", "count in main thread: $count")
                binding.bottomNavigation.setCount(ID_Favorite, "$count")
            }

        }
        mFavoriteCount = 5
        BottomNavigation()
        showFirstFragment()
        showListenNameItem()
        selectItemMenu()
    }

    override fun onResume() {
        super.onResume()
        val intValueToDisplay: Int = count
//        mFavoriteCount = intValueToDisplay
        Log.e("ww","mFavoriteCount = $mFavoriteCount")
    }
//    private fun updateNumber(count: Int?) {

//        Log.e("ww","intValueToDisplay = $intValueToDisplay")
//
//    }

    private fun showListenNameItem() {
        binding.bottomNavigation.setOnShowListener {
            when (it.id) {
                ID_Home -> {
                    name = "Home"
                }
                ID_Favorite -> {
                    name = "Favorite"
                }
                ID_Message -> {
                    name = "Message"
                }
                ID_Account -> {
                    name = "Account"
                }
                else -> {
                    name = ""
                }
            }
        }
    }

    private fun showFirstFragment() {
        replace(HomeFragment(1))
        binding.bottomNavigation.show(ID_Home, true)
    }

    private fun selectItemMenu() {
        binding.bottomNavigation.setOnClickMenuListener {
//            Toast.makeText(this,name,Toast.LENGTH_SHORT).show()
            when (it.id) {
                ID_Home -> {
                    replace(HomeFragment(1))
                }
                ID_Favorite -> {
                    replace(FavoriteFragment())
                }
                ID_Message -> {
                    replace(MessageFragment())
                }
                ID_Account -> {
                    replace(AccountFragment())
                }
                else -> {

                }
            }
        }
    }

    private fun replace(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.viewPager, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun BottomNavigation() {
        binding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                ID_Home,
                R.drawable.ic_baseline_home_24
            )
        )
        binding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                ID_Favorite,
                R.drawable.ic_baseline_favorite_24
            )
        )
        binding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                ID_Message,
                R.drawable.ic_baseline_message_24
            )
        )
        binding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                ID_Account,
                R.drawable.ic_baseline_account_circle_24
            )
        )
//        binding.bottomNavigation.setCount(ID_Account, "4")
        binding.bottomNavigation.setCount(ID_Message, "4")
        binding.bottomNavigation.setCount(ID_Favorite, "${mFavoriteCount}")
    }

//    override fun onUpdateBadgeNumber(isFavorite: Boolean) {
//        if (isFavorite)
//            binding.bottomNavigation.setCount(ID_Favorite, "${++mFavoriteCount}")
//        else
//            binding.bottomNavigation.setCount(ID_Favorite, "${--mFavoriteCount}")
//    }

//    override fun onUpdateFromDetail(movie: Movie, isFavorite: Boolean) {
//        mHomeFragment.updateMovieList(movie, isFavorite)
//        mfavoriteFragment.updateFavoriteList(movie, isFavorite)
//    }

//    override fun onAddReminder() {
//        mMovieReminderList = mDatabaseOpenHelper.getListReminder()
//        mReminderAdapter.updateData(mMovieReminderList)
//    }


//    override fun onUpdateFromFavorite(movie: Movie) {
//        mHomeFragment.updateMovieList(movie, false)
//    }
//
//    override fun onUpdateFromMovie(movie: Movie, isFavorite: Boolean) {
//        mfavoriteFragment.updateFavoriteList(movie, isFavorite)
//    }

}