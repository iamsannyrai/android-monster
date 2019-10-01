package com.example.managedata.ui.main.splash


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.managedata.R

class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        displayMainFragment()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


    private fun displayMainFragment() {
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        navController.navigate(
            R.id.action_splashFragment_to_mainFragment, null, NavOptions.Builder()
                .setPopUpTo(R.id.splashFragment, true)
                .build()
        )
    }
}
