package com.example.filmtracker.view.home.fragment.settingfragment

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.filmtracker.R
import com.example.filmtracker.databinding.DialogCategoryBinding
import com.example.filmtracker.databinding.DialogReleaseYearBinding
import com.example.filmtracker.databinding.DialogSortBinding
import com.example.filmtracker.databinding.FragmentSettingBinding
import com.example.filmtracker.models.Constant
import com.example.filmtracker.view.home.fragment.homefragment.HomeFragment
import org.greenrobot.eventbus.EventBus

class SettingFragment : Fragment(){

    private var radioButtonDialog: AlertDialog? = null
    private lateinit var binding: FragmentSettingBinding
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        binding.skbRate.progress = mSharedPreferences.getInt(Constant.PREF_RATE_KEY,0)
        loadDataFromShared()

        categoryClickListener()
        releaseYearClickListenerO()
        sortClickListener()
        rateSeekbarListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backtoHomeFragment()
    }

    private fun loadDataFromShared(){
        binding.txtCate.setText(mSharedPreferences.getString(Constant.PREF_CATEGORY_KEY,"No Data"))
        binding.txtRate.setText(mSharedPreferences.getInt(Constant.PREF_RATE_KEY,0).toString())
        binding.txtReleaseYear.setText(mSharedPreferences.getString(Constant.PREF_RELEASE_KEY," "))
        binding.txtSortBy.setText(mSharedPreferences.getString(Constant.PREF_SORT_KEY," "))
    }

    private fun rateSeekbarListener() {
        binding.skbRate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Cập nhật giá trị trong TextView khi giá trị của SeekBar thay đổi
                binding.txtRate.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Không cần xử lý
            }
        })
    }

    private fun sortClickListener() {
        binding.layoutSort.setOnClickListener {
            dialogSort()
        }
    }

    private fun dialogSort() {
        val sortBinding = DialogSortBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext())
        val radioGroup = sortBinding.radioGroup
        builder.setView(sortBinding.root)
            .setTitle("Category")
            .setPositiveButton("OK") { dialog, which ->
                val selectedRadioButtonId = radioGroup.checkedRadioButtonId
                val selectedOption = when(selectedRadioButtonId) {
                    R.id.rdoNone-> sortBinding.rdoNone.text.toString()
                    R.id.rdoReleaseDate -> sortBinding.rdoReleaseDate.text.toString()
                    R.id.rdoRating -> sortBinding.rdoRating.text.toString()
                    else -> ""
                }
                binding.txtSortBy.text = selectedOption
            }
            .setNegativeButton("Cancel") { dialog, which ->
                radioButtonDialog?.dismiss()
            }
            .show()

    }

    private fun releaseYearClickListenerO() {
        binding.layoutRelease.setOnClickListener {
            dialogRelease()
        }
    }

    private fun dialogRelease() {
        val releaseYearBinding = DialogReleaseYearBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(releaseYearBinding.root)
            .setTitle("Category")
            .setPositiveButton("OK") { dialog, which ->
                binding.txtReleaseYear.text = releaseYearBinding.edtReleaseYear.text
            }
            .setNegativeButton("Cancel") { dialog, which ->
                radioButtonDialog?.dismiss()
            }
            .show()
    }

    private fun categoryClickListener(){
        binding.layoutCategory.setOnClickListener {
            dialogCategory()
        }
    }
    private fun dialogCategory() {
        val categoryBinding = DialogCategoryBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext())
        val radioGroup = categoryBinding.radioGroup
        builder.setView(categoryBinding.root)
            .setTitle("Category")
            .setPositiveButton("OK") { dialog, which ->
                val selectedRadioButtonId = radioGroup.checkedRadioButtonId
                val selectedOption = when(selectedRadioButtonId) {
                    R.id.radioOption1 -> categoryBinding.radioOption1.text.toString()
                    R.id.radioOption2 -> categoryBinding.radioOption2.text.toString()
                    R.id.radioOption3 -> categoryBinding.radioOption3.text.toString()
                    R.id.radioOption4 -> categoryBinding.radioOption4.text.toString()
                    else -> ""
                }
                binding.txtCate.text = selectedOption
//
            }
            .setNegativeButton("Cancel") { dialog, which ->
                radioButtonDialog?.dismiss()
            }
            .show()
    }

    private fun upSettingToShared(
        category:String,
        rate: Int,
        releaseYear:String,
        sortBy:String
    ){
        val edit = mSharedPreferences.edit()
        edit.putString(Constant.PREF_CATEGORY_KEY, category)
        edit.putInt(Constant.PREF_RATE_KEY, rate)
        edit.putString(Constant.PREF_RELEASE_KEY, releaseYear)
        edit.putString(Constant.PREF_SORT_KEY, sortBy)
        edit.apply()
    }

    private fun backtoHomeFragment(){
        binding.acceptSetting.setOnClickListener {
            val category = binding.txtCate.text.toString()
            val rate = binding.txtRate.text.toString()
            val releaseYear = binding.txtReleaseYear.text.toString()
            val sortBy = binding.txtSortBy.text.toString()
            upSettingToShared(category,rate.toInt(), releaseYear,sortBy)
            Log.e("ii","Category: $category")
            Log.e("ii","Rate: $rate")
            Log.e("ii","Release Year: $releaseYear")
            Log.e("ii","Sort By: $sortBy")
            val c = HomeFragment(1)
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.viewPager,c)
                addToBackStack(null)
                commit()
            }
            EventBus.getDefault().post(UpdateListMovie(true))
        }
    }

    class UpdateListMovie(val isUpdate : Boolean)
}