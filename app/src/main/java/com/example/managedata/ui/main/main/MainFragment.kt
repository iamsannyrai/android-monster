package com.example.managedata.ui.main.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.managedata.R
import com.example.managedata.data.Monster
import com.example.managedata.ui.main.shared.SharedViewModel

class MainFragment : Fragment(),
    MainRecyclerAdapter.MonsterItemListener {

    private lateinit var viewModel: SharedViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        val view = inflater.inflate(R.layout.main_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        swipeLayout = view.findViewById(R.id.swipeLayout)
        swipeLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
        viewModel.monsterData.observe(this, Observer {
            val adapter =
                MainRecyclerAdapter(requireContext(), it, this)
            recyclerView.adapter = adapter
            swipeLayout.isRefreshing = false
        })
        return view
    }

    override fun onMonsterItemClick(monster: Monster) {
        viewModel.selectedMonster.value = monster
        navController.navigate(R.id.action_mainFragment_to_detailFragment)
    }
}
