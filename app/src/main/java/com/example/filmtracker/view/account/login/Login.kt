package com.example.filmtracker.view.account.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.filmtracker.R
import com.example.filmtracker.databinding.ActivityLoginBinding
import com.example.filmtracker.models.Finger
import com.example.filmtracker.view.account.register.Register
import com.example.filmtracker.view.home.Home
import com.google.firebase.database.*
import java.util.concurrent.Executor
import android.util.Pair as UtilPair

@RequiresApi(Build.VERSION_CODES.P)
class Login : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var flag: Boolean? = null
    private var fingers: ArrayList<Finger>? = null
    private var database: DatabaseReference? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        database = FirebaseDatabase.getInstance().getReference("FilmTracker")
        fingers = arrayListOf()
        //Event Click
        LoginClickListener()
        RegisterClickListener()
        FingerClickListener()

    }

    private fun LoginClickListener() {
        binding.rlt.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

    }

    private fun RegisterClickListener() {
        binding.signupScreen.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    UtilPair.create(binding.logoImage, "logo_image"),
                    UtilPair.create(binding.nameLogo, "name_image"),
                    UtilPair.create(binding.sloganName, "logo_desc"),
                    UtilPair.create(binding.username, "username_tran"),
                    UtilPair.create(binding.password, "password_tran"),
                    UtilPair.create(binding.rlt, "button_login"),
                    UtilPair.create(binding.signupScreen, "button_signup")
                )
                startActivity(intent, options.toBundle())
            }
        }
    }

    private fun FingerClickListener() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        applicationContext,
                        "Authentication succeeded!",
                        Toast.LENGTH_SHORT
                    ).show()
//                    flag = true
//                    database!!.child("Login").child("finger").setValue(flag)
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        flag = false
//                        database!!.child("Login").child("finger").setValue(flag)
//                    }, 5000)
                    val intent = Intent(applicationContext, Home::class.java)
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Xác thực danh tính của bạn")
            .setSubtitle("Sử dụng vân tay để xác thực danh tính của bạn.")
            .setNegativeButtonText("Hủy")
            .build()

        binding.fingerprint.setOnClickListener {
//            database!!.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (usesnapshot in snapshot.children) {
//                        val x = usesnapshot.getValue(Finger::class.java)
//                        fingers!!.add(x!!)
//                        for (i in 0 until fingers!!.size) {
//                            if (fingers!!.get(i).finger == true) {
//
//                            }
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })

            biometricPrompt.authenticate(promptInfo)

        }
    }

}