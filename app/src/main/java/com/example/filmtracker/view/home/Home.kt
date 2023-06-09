package com.example.filmtracker.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.filmtracker.R
import com.example.filmtracker.databinding.ActivityHomeBinding
import com.example.filmtracker.view.home.fragment.accountfragment.AccountFragment
import com.example.filmtracker.view.home.fragment.favoritefragment.FavoriteFragment
import com.example.filmtracker.view.home.fragment.homefragment.HomeFragment
import com.example.filmtracker.view.home.fragment.messagefragment.MessageFragment
import com.example.filmtracker.view.home.fragment.settingfragment.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    private lateinit var mDrawerLayout: DrawerLayout

    private lateinit var mHomeFragment: HomeFragment
    private lateinit var mfavoriteFragment: FavoriteFragment
    private lateinit var mMessageFragment: MessageFragment
    private lateinit var mAccountFragment: AccountFragment
    private lateinit var mSettingFragment: SettingFragment

    private var ID_Home = 1
    private var ID_Favorite = 2
    private var ID_Message = 3
    private var ID_Account = 4
    private var name = ""
//    private var bottomNavigation: MeowBottomNavigation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        //Fragment view
        mHomeFragment = HomeFragment(1)
        mfavoriteFragment = FavoriteFragment()
        mMessageFragment = MessageFragment()
        mSettingFragment = SettingFragment()
        mAccountFragment = AccountFragment()

        BottomNavigation()
        showFirstFragment()
        showListenNameItem()
        selectItemMenu()
    }

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
        binding.bottomNavigation.setCount(ID_Favorite, "10")
    }
}