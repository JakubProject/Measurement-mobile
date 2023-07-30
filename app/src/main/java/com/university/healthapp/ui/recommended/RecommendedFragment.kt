package com.university.healthapp.ui.recommended

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.university.healthapp.databinding.FoodBinding
import com.university.healthapp.databinding.ListOfMeasurementsBinding
import com.university.healthapp.databinding.RecommendedProductBinding
import com.university.healthapp.ui.food.FoodModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class RecommendedFragment : Fragment() {

    private var _binding: RecommendedProductBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecommendedProductBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}