package com.example.filmtracker.view.home.fragment.accountfragment

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmtracker.R
import com.example.filmtracker.databinding.FragmentAccountBinding
import com.example.filmtracker.models.Movie
import com.example.filmtracker.utility.BitmapConverter
import com.example.filmtracker.view.home.fragment.MovieViewModel
import com.example.filmtracker.view.home.fragment.MovieViewModelFactory
import java.lang.Exception


class AccountFragment : Fragment() {
    private val imgConverter: BitmapConverter = BitmapConverter()
    private lateinit var binding: FragmentAccountBinding
    private lateinit var mSharedPreferences: SharedPreferences
    private var mListRemind: ArrayList<Movie> = arrayListOf()
    private lateinit var mReminderAdapter: ReminderAdapter
    private val noteViewModel: MovieViewModel by lazy {
        ViewModelProvider(this, MovieViewModelFactory(requireActivity().getApplication())).get(
            MovieViewModel::class.java
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        val rootView = binding.root
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        loadDataProfile()
        noteViewModel.getAllRemind()

        loadRemindList()

        binding.editProfileBtn.setOnClickListener {
            val editFragment = EditAccountFragment()
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.viewPager,editFragment)
                addToBackStack(null)
                commit()
            }
        }

        binding.btnShowReminderAll.setOnClickListener {
            val remindFragment = RemindAllFragment()
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.viewPager,remindFragment)
                addToBackStack(null)
                commit()
            }
        }
        return rootView
    }

    private fun loadRemindList() {
        mReminderAdapter = ReminderAdapter(requireActivity(),ReminderAdapter.REMINDER_PROFILE, onClick, onLongClick)
        binding.rcvViewReminder.layoutManager = LinearLayoutManager(requireActivity())
        binding.rcvViewReminder.setHasFixedSize(true)
        binding.rcvViewReminder.adapter = mReminderAdapter

        noteViewModel.remindState.observe(requireActivity()) {
            mReminderAdapter.updateData(it)
            mReminderAdapter.notifyDataSetChanged()
            mListRemind.clear()
            mListRemind.addAll(it)
        }
    }

    private val onClick: (Movie) -> Unit = {

    }
    private val onLongClick: (Movie) -> Unit = {

    }

    override fun onPause() {
        super.onPause()
        Log.e("kiemtra", "pause Account Frag")
    }

    override fun onStop() {
        super.onStop()
        Log.e("kiemtra", "stop Account Frag")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("kiemtra", "Destroy Account Frag")
    }

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