package com.university.healthapp


// OBIEKT DO KOMUNIKACJI Z API
data class MeasurementData(
    val id: Int = -1,
    val pressure: String = "TEST",
    val weight: String = "TEST",
    val status: String = "TEST",
    val dietType: String = "TEST",
    val woman: Boolean = true,
    val date: String = "TEST"
)