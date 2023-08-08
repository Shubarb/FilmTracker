package com.example.filmtracker.view.home.fragment.accountfragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.filmtracker.R
import com.example.filmtracker.databinding.FragmentEditAccountBinding
import com.example.filmtracker.databinding.FragmentFavoriteBinding
import com.example.filmtracker.utility.BitmapConverter
import org.greenrobot.eventbus.EventBus
import java.lang.Exception
import java.util.*

class EditAccountFragment : Fragment() {

    private lateinit var binding: FragmentEditAccountBinding

    private var mBitmapProfile: Bitmap? = null
    private val mConverterImg: BitmapConverter = BitmapConverter()
    private var mGender: String = ""
    private var mSaveDay = 0
    private var mSaveMonth = 0
    private var mSaveYear = 0

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("checkData", "on create view")
        binding = FragmentEditAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("checkData", "on view created")
        super.onViewCreated(view, savedInstanceState)

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)

        onClickChanged()

        loadDataProfile()

        binding.txtDobProfile.setOnClickListener{
            setDate()
        }

        binding.saveProfile.setOnClickListener {
            var name = binding.edtNameProfile.text.toString()
            var email = binding.edtEmailProfile.text.toString()
            var dob = binding.txtDobProfile.text.toString()
            var img = mBitmapProfile
            var gender = mGender
            updateAccountFromEdit(name,email,dob,gender,img)
            val editFragment = AccountFragment()
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.viewPager,editFragment)
                addToBackStack(null)
                commit()
            }
        }

        binding.imgAvatarEditProfile.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun loadDataProfile() {
        binding.edtNameProfile.setText(mSharedPreferences.getString("profileName", "No data"))
        binding.edtEmailProfile.setText(mSharedPreferences.getString("profileEmail", "No data"))
        binding.txtDobProfile.text = mSharedPreferences.getString("profileDob", "No data")

        if(mSharedPreferences.getString("profileGender", "No data") == "Female"){
            binding.radioGenderGr.check(R.id.radio_female)
        }else{
            binding.radioGenderGr.check(R.id.radio_male)
        }
        try {
            binding.imgAvatarEditProfile.setImageBitmap(
                mConverterImg.decodeBase64(
                    mSharedPreferences.getString(
                        "profileImg",
                        "No data"
                    )
                )
            )
        } catch (e: Exception) {
            binding.imgAvatarEditProfile.setImageResource(R.drawable.ic_baseline_person_24)
        }
    }

    fun updateAccountFromEdit(
        name: String,
        email: String,
        dob: String,
        gender: String,
        imgBitmap: Bitmap?
    ) {
        val edit = mSharedPreferences.edit()
        edit.putString("profileName", name)
        edit.putString("profileEmail", email)
        edit.putString("profileDob", dob)
        edit.putString("profileGender", gender)
        if (imgBitmap != null)
            edit.putString("profileImg", mConverterImg.encodeBase64(imgBitmap))
        this.mBitmapProfile = imgBitmap
        binding.imgAvatarEditProfile.setImageBitmap(imgBitmap)
        edit.apply()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
        }catch (e: ActivityNotFoundException){

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            this.mBitmapProfile = imageBitmap
            binding.imgAvatarEditProfile.setImageBitmap(imageBitmap)
        }

    }

    private fun setDate() {
        val currentDataTime = Calendar.getInstance()
        val startYear = currentDataTime.get(Calendar.YEAR)
        val startMonth = currentDataTime.get(Calendar.MONTH)
        val startDay = currentDataTime.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(requireContext(),{ _,year,month,day ->
            val pickedDateTime = Calendar.getInstance()
            pickedDateTime.set(year,month,day)
            mSaveYear = year
            mSaveMonth = month
            mSaveDay = day
            currentDataTime.set(mSaveYear,mSaveMonth,mSaveDay)
            val timehour = "$mSaveYear/${mSaveMonth+1}/$mSaveDay"
            binding.txtDobProfile.setText(timehour,TextView.BufferType.EDITABLE)
        },startYear,startMonth,startDay).show()
    }

    private fun onClickChanged() {
        binding.radioGenderGr.setOnCheckedChangeListener { _, i ->
            if(i == R.id.radio_male){
                mGender = "Male"
            }else if(i == R.id.radio_female){
                mGender = "Female"
            }
        }
    }


}