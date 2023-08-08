package com.example.filmtracker.view.home.fragment.accountfragment

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmtracker.R
import com.example.filmtracker.databinding.FragmentAccountBinding
import com.example.filmtracker.databinding.FragmentFavoriteBinding
import com.example.filmtracker.models.Constant
import com.example.filmtracker.models.Constants
import com.example.filmtracker.utility.BitmapConverter
import com.example.filmtracker.view.home.fragment.detailfragment.DetailFragment
import java.lang.Exception


class AccountFragment : Fragment() {
    private var imgBitmap: Bitmap? = null
    private val imgConverter: BitmapConverter = BitmapConverter()
    private lateinit var binding: FragmentAccountBinding
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        val rootView = binding.root
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        loadDataProfile()
        binding.editProfileBtn.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("name", binding.tvName.text.toString())
//            bundle.putString("email", binding.txtMailHeaderProfile.text.toString())
//            bundle.putString("dob", binding.txtDobHeaderProfile.text.toString())
//            bundle.putString("gender", binding.txtGenderHeaderProfile.text.toString())
//            try {
//                bundle.putString(
//                    "imgBitMapString",
//                    mSharedPreferences.getString("profileImg", "No data")
//                )
//            } catch (e: Exception) {
//
//            }
            val editFragment = EditAccountFragment()
//            editFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.viewPager,editFragment)
                addToBackStack(null)
                commit()
            }
        }
        return rootView
    }

//    fun updateAccountFromEdit(
//        name: String,
//        email: String,
//        dob: String,
//        gender: String,
//        imgBitmap: Bitmap?
//    ) {
//        val edit = mSharedPreferences.edit()
//        edit.putString("profileName", name)
//        edit.putString("profileEmail", email)
//        edit.putString("profileDob", dob)
//        edit.putString("profileGender", gender)
//        if (imgBitmap != null)
//            edit.putString("profileImg", imgConverter.encodeBase64(imgBitmap))
//        this.imgBitmap = imgBitmap
//        binding.imgAvatar.setImageBitmap(imgBitmap)
//        edit.apply()
////        loadDataProfile()
//    }

    private fun loadDataProfile() {
        binding.tvName.text = mSharedPreferences.getString("profileName", "No data")
        binding.txtMailHeaderProfile.text = mSharedPreferences.getString("profileEmail", "No data")
        binding.txtDobHeaderProfile.text = mSharedPreferences.getString("profileDob", "No data")
        binding.txtGenderHeaderProfile.text = mSharedPreferences.getString("profileGender", "No data")
        try {
            binding.imgAvatar.setImageBitmap(
                imgConverter.decodeBase64(
                    mSharedPreferences.getString(
                        "profileImg",
                        "No data"
                    )
                )
            )
        } catch (e: Exception) {
            binding.imgAvatar.setImageResource(R.drawable.ic_baseline_person_24)
        }
    }

}