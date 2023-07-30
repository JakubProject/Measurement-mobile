package com.university.healthapp.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.university.healthapp.databinding.FoodBinding

class FoodFragment : Fragment() {

    private var _binding: FoodBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val foodModel =
            ViewModelProvider(this).get(FoodModel::class.java)

        println(foodModel)
        println(inflater)
        println(container)

        _binding = FoodBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}