package com.university.healthapp

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.university.healthapp.databinding.ActivityMainBinding
import com.university.healthapp.databinding.MeasurementBinding
import com.university.healthapp.ui.adapter.ItemAdapter
import com.university.healthapp.ui.adapter.RecommendedAdapter
import com.university.healthapp.ui.data.RecommendedModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.lang.Exception
import java.time.LocalDate


// GŁÓWNA KLASA W KOTLINIE
class MainActivity : AppCompatActivity() {

    // ZMIENNE POMOCNICZE

    private lateinit var binding: ActivityMainBinding

    private lateinit var measurementBinding: MeasurementBinding

    private var nav : Boolean = false

    // OBSŁUGA LOGIKI APLIKACJI
    // POŁĄCZENIE WIDOKÓW Z KLASAMI W KOTLINIE
    // FUNKCJA KTÓRA JEST URUCHAMIANA NA STARCIE URUCHAMIANIA PROGRAMU
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // WIDOK GŁÓWNY I POMIARU
        binding = ActivityMainBinding.inflate(layoutInflater)
        measurementBinding = MeasurementBinding.inflate(layoutInflater)

        println("START ActivityMainBinding")
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_recommended, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        // OBSŁUGA PRZYCISKÓW W APLIKACJI
        measurementBinding.calculateButton.setOnClickListener { calculateTip() }

        // Set up a key listener on the EditText field to listen for "enter" button presses
        measurementBinding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(
                view,
                keyCode
            )
        }
        setContentView(measurementBinding.root)

        initRecyclerView()
    }


    // ZMIANA WIDOKU, POKAZANIE NAV BARU
    private fun changeView() {
        if(nav === false){
            setContentView(binding.root)
        }
        else{
            setContentView(measurementBinding.root)
        }
    }

    // OBLICZENIE TYPU DIETY JAKA POWINNA BYC
    private fun calculateTip() {
        val dietType : DietType

        if(measurementBinding.costOfServiceEditText.text.toString().toInt() > 80
            || measurementBinding.costOfServiceEditText1.text.toString().toInt() > 120)
            dietType = DietType.EXTRA
        else if (measurementBinding.costOfServiceEditText.text.toString().toInt() < 80 &&
            measurementBinding.costOfServiceEditText1.text.toString().toInt() < 120 &&
            measurementBinding.tipOptions.checkedRadioButtonId-2131231060 == 0)
            dietType = DietType.HEALTHY
        else
            dietType = DietType.NORMAL

        displayTip(dietType)
        saveData(dietType)
        changeView()
    }

    // ZAPIS DANYCH DO API
    private fun getData(){
        // WYŁĄCZENIE ZABEZPIECZEŃ
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val objectMapper = ObjectMapper()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON


        val restTemplate = RestTemplate()
        val rest: ResponseEntity<String> = restTemplate.exchange<String>(
            "https://app1.takemewith.pl/clients",
            HttpMethod.GET,
            HttpEntity<Any?>(headers),
            String::class.java
        )
        println("DANE ZOSTAŁY POBRANE")
    }

    // ZAPIS DANYCH DO API
    private fun saveData(dietType: DietType){
        // request data
        var weight = measurementBinding.costOfServiceEditText.text.toString();
        var pressure = measurementBinding.costOfServiceEditText1.text.toString();
        var status = measurementBinding.tipOptions.checkedRadioButtonId-2131231060;
        var woman = measurementBinding.roundUpSwitch.isChecked;

        var statusString = "kot";
        if(status == 0){
            statusString = "OKAY";
        }
        else if(status == 1){
            statusString = "GOOD";
        }
        else{
            statusString = "BAD";
        }

        // save data
        val restTemplate = RestTemplate()
        var measurement = MeasurementData(1,weight,pressure,statusString,dietType.toString(),woman,LocalDate.now().toString())
        print("API CALL")
        print(measurement.toString())
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val objectMapper = ObjectMapper()

        // WYŁĄCZENIE ZABEZPIECZEŃ
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // WYWOŁANIE ZEWNĘTRZNEGO API I ZAPIS DO BAZY DANYCH
        try {
            val rest: ResponseEntity<String> = restTemplate.exchange<String>(
                "https://app1.takemewith.pl/measurement",
                HttpMethod.POST,
                HttpEntity<Any?>(objectMapper.writeValueAsString(measurement), headers),
                String::class.java
            )
        }catch (e:Exception){
            println("DATA NOT ADDED")
        }

        print("DATA ADDED")
    }

    // WYŚWIETLANIE TYPU DIETY JAKĄ UŻYTKOWNIK POWINNIEN MIEĆ
    private fun displayTip(dietType: DietType) {
        measurementBinding.tipResult.text = "Diet type is: " + dietType.toString()
    }

    // OBSŁUGA KLAWIATURY
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

    // OBSŁUGA ADAPTERA
    private fun initRecyclerView() {
        var recyclerAdapter: RecommendedAdapter
        this.binding.recommendedRecyclerView.layoutManager =LinearLayoutManager(this)
        recyclerAdapter = RecommendedAdapter(this)
        this.binding.recommendedRecyclerView.adapter =recyclerAdapter
        recyclerAdapter.setName(RecommendedModel("test12"))
    }

}