package com.example.filmtracker.view.splash

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.filmtracker.R
import com.example.filmtracker.databinding.ActivitySplashScreenBinding
import com.example.filmtracker.view.account.login.Login
import android.util.Pair as UtilPair


class SplashScreen : AppCompatActivity() {

    private var SPLASH_SCREEN: Long = 2500
    lateinit var binding: ActivitySplashScreenBinding
    private var topAnim: Animation? = null
    private var bottomAnim: Animation? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation)
        binding.imgLogo.animation = topAnim
        binding.nameLogo.animation = bottomAnim

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Login::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptions.makeSceneTransitionAnimation(this,
                    UtilPair.create(binding.imgLogo, "logo_image"),
                    UtilPair.create(binding.nameLogo,"name_image")
                )
                startActivity(intent, options.toBundle())
            }
            startActivity(intent)
        }, SPLASH_SCREEN)

    }
}