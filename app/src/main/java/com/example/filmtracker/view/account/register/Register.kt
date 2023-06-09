package com.example.filmtracker.view.account.register

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.example.filmtracker.R
import com.example.filmtracker.databinding.ActivityRegisterBinding
import com.example.filmtracker.view.account.login.Login
import com.google.android.material.textfield.TextInputLayout
import android.util.Pair as UtilPair

class Register : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        //Set change
        DoOnTextChange()
        //Set on click listener event
        setOnClick()
    }

    private fun DoOnTextChange() {
        //EditText Enter
        binding.usernameResis.doOnTextChanged { text, start, before, count ->
            binding.usernameLayout.isCounterEnabled = true
            binding.usernameLayout.isEndIconVisible = true
            binding.usernameLayout.isStartIconVisible = true
            binding.usernameLayout.isErrorEnabled = true
            binding.usernameLayout.setStartIconDrawable(R.drawable.ic_baseline_person_gray_24)
            if (text!!.length > 9) {
                binding.usernameLayout.error = "No More"
            } else if (text.length < 10) {
                binding.usernameLayout.error = null
            }
            binding.usernameLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT)

        }

        binding.fullname.doOnTextChanged { text, start, before, count ->
            binding.fullnameLayout.isEndIconVisible = true
            binding.fullnameLayout.isStartIconVisible = true
            binding.fullnameLayout.setStartIconDrawable(R.drawable.ic_baseline_account_circle_24)
            binding.fullnameLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT)
            if (text!!.length > 0) {
                binding.fullnameLayout.error = null
                binding.fullnameLayout.isErrorEnabled = false
            }
        }

        binding.email.doOnTextChanged { text, start, before, count ->
            binding.emailLayout.isEndIconVisible = true
            binding.emailLayout.isStartIconVisible = true
            binding.emailLayout.setStartIconDrawable(R.drawable.ic_baseline_email_24)
            binding.emailLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT)
            if (text!!.length > 0) {
                binding.emailLayout.error = null
                binding.emailLayout.isErrorEnabled = false
            }
        }

        binding.phonenumber.doOnTextChanged { text, start, before, count ->
            binding.phonenumberLayout.isEndIconVisible = true
            binding.phonenumberLayout.isStartIconVisible = true
            binding.phonenumberLayout.setStartIconDrawable(R.drawable.ic_baseline_phone_24)
            binding.phonenumberLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT)
            if (text!!.length > 0) {
                binding.phonenumberLayout.error = null
                binding.phonenumberLayout.isErrorEnabled = false
            }
        }

        binding.passwordResis.doOnTextChanged { text, start, before, count ->
            binding.passlayout.isEndIconVisible = true
            binding.passlayout.isStartIconVisible = true
            binding.passlayout.setStartIconDrawable(R.drawable.ic_baseline_lock_24)
            if (text!!.length > 0) {
                binding.passlayout.error = null
                binding.passlayout.isErrorEnabled = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setOnClick() {
        //Register
        binding.rltResis.setOnClickListener {

        }

        //Back to login
        binding.loginScreen.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    UtilPair.create(binding.logoImage, "logo_image"),
                    UtilPair.create(binding.nameLogo, "name_image"),
                    UtilPair.create(binding.sloganName, "logo_desc"),
                    UtilPair.create(binding.usernameResis, "username_tran"),
                    UtilPair.create(binding.passwordResis, "password_tran"),
                    UtilPair.create(binding.rltResis, "button_login"),
                    UtilPair.create(binding.loginScreen, "button_signup")
                )
                startActivity(intent, options.toBundle())
            }
        }
    }


}