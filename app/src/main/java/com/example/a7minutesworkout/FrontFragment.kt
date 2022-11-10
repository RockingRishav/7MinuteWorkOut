package com.example.a7minutesworkout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.example.a7minutesworkout.databinding.FragmentFrontBinding

class FrontFragment : Fragment() {
    private var _binding : FragmentFrontBinding? = null
    private val binding get() = _binding!!
//    lateinit var drawerLayout: DrawerLayout
//    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFrontBinding.inflate(inflater,container,false)
               (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


//        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbarFront)


        if((activity as AppCompatActivity?)!!.supportActionBar != null){
            (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
//        binding.toolbarFront.setNavigationOnClickListener {
//           activity?.onBackPressed()
//        }
        binding.flStart.setOnClickListener{
//            Toast.makeText(this@MainActivity,

            val action = FrontFragmentDirections.actionFrontFragmentToExerciseFragment()
            view.findNavController().navigate(action)

        }
        binding.flBMI.setOnClickListener{
             val action = FrontFragmentDirections.actionFrontFragmentToBmiFragment()
            view.findNavController().navigate(action)
        }
        binding.history.setOnClickListener {
            val intent = Intent(activity, HistoryActivity::class.java )
            startActivity(intent)
        }
        binding.locateGym.setOnClickListener {
            val intent = Intent(activity, MapActivity::class.java )
            startActivity(intent)

        }
    }
//override fun onOptionsItemSelected(item: MenuItem): Boolean {
//    return if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
//        true
//    }
//    else super.onOptionsItemSelected(item)
//
//}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}