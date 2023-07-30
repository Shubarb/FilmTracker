package com.example.filmtracker.view.home.fragment.detailfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.filmtracker.databinding.FragmentDetailBinding

class DetailFragment(
//    private var mDataBaseOpenHelper: DataBaseOpenHelper
    ) : Fragment(),View.OnClickListener {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root


        return rootView
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}