package com.university.healthapp.ui.adapter

import android.app.Activity
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.databind.ObjectMapper
import com.university.healthapp.MeasurementData
import com.university.healthapp.R
import com.university.healthapp.databinding.ActivityMainBinding.bind
import com.university.healthapp.ui.data.RecommendedModel
import com.university.healthapp.ui.measurements.MeasurementFragment
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.lang.Exception


class RecommendedAdapter(val activity: Activity): RecyclerView.Adapter<RecommendedAdapter.MyViewHolder>() {

    private var name: RecommendedModel? = null


    fun setName(name: RecommendedModel) {
        this.name = name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recommended_product, parent, false)
        val result = MyViewHolder(view)
        return result
    }

    override fun getItemCount(): Int {
        return 1;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind();
    }


    class MyViewHolder(view : View): RecyclerView.ViewHolder(view){
        val nameOfProduct: TextView = view.findViewById(R.id.product_name)
        val dietType: TextView = view.findViewById(R.id.diet_type)
        fun bind() {
            nameOfProduct.text = getDailyRecommendedFood();
            dietType.text = getDietType();
        }

        // ZALECANY DZIENNY POSIŁEK
        private fun getDailyRecommendedFood(): String{
            // WYŁĄCZENIE ZABEZPIECZEŃ
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val objectMapper = ObjectMapper()
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON

            // POBRANIE DANYCH O POMIARACH
            val restTemplate = RestTemplate()
            val rest: ResponseEntity<String> = restTemplate.exchange<String>(
                "https://app1.takemewith.pl/getLast",
                HttpMethod.GET,
                HttpEntity<Any?>(headers),
                String::class.java
            )
            var responseObject : MeasurementData = objectMapper.readValue(rest.body, MeasurementData::class.java)
            println("DANE ZOSTAŁY POBRANE")
            var response = responseObject.status;
            if(response == "OKAY"){
                response = "Pizza";
            }
            else if (response == "GOOD"){
                response = "Banana";
            }
            else{
                response = "Chocolate"
            }
            return response;
        }

        private fun getDietType(): String{
            // WYŁĄCZENIE ZABEZPIECZEŃ
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val objectMapper = ObjectMapper()
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON

            // POBRANIE DANYCH O POMIARACH
            val restTemplate = RestTemplate()
            try {
                val rest: ResponseEntity<String> = restTemplate.exchange<String>(
                    "https://app1.takemewith.pl/getLast",
                    HttpMethod.GET,
                    HttpEntity<Any?>(headers),
                    String::class.java
                )
                var responseObject : MeasurementData = objectMapper.readValue(rest.body, MeasurementData::class.java)
                return responseObject.dietType;
            }catch (e: Exception){
                println("LAST 5 DAYS")
            }
            println("DANE ZOSTAŁY POBRANE")
            return "NO DATA";
        }



    }




}

