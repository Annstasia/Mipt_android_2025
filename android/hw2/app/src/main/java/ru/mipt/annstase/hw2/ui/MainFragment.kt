package ru.mipt.annstase.hw2.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.mipt.annstase.hw2.R
import ru.mipt.annstase.hw2.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var childNavController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMainBinding.bind(view)

        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.child_nav_host) as NavHostFragment
        childNavController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(childNavController)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.aboutFragment)
        }


    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
