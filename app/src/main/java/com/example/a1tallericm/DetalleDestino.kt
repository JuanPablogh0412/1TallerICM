package com.example.a1tallericm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class DetalleDestino : AppCompatActivity() {
    private val apiKey = "fdf94cd5d591c99bf379abcc5ea7a509"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_destino)
        val bundle = intent.getBundleExtra("bundle")
        val nombre: String = bundle?.getString("Nombre").toString()
        val pais: String = bundle?.getString("Pais").toString()
        val plan = bundle?.getString("Plan")
        val precio = bundle?.getInt("Precio")
        val categoria: String = bundle?.getString("Categoria").toString()
        val favoritoButton: Button = findViewById(R.id.addfavoritos)
        findViewById<TextView>(R.id.Nombre).text = nombre
        findViewById<TextView>(R.id.pais).text = pais
        findViewById<TextView>(R.id.plan).text = plan
        findViewById<TextView>(R.id.precio).text = "USD "+precio.toString()
        CoroutineScope(Dispatchers.IO).launch {
            obtenerClima(pais)
        }
        clickFavoritos(favoritoButton, nombre, categoria)
    }

    private fun clickFavoritos(favoritoButton: Button, nombre: String, categoria: String) {
        favoritoButton.setOnClickListener {
            MainActivity.favoritos.add(nombre ?: "")
            when (categoria) {
                "Playas" -> MainActivity.countPlayas++
                "Montañas" -> MainActivity.countMontanas++
                "Ciudades Históricas" -> MainActivity.countCiudadesHistoricas++
                "Maravillas del Mundo" -> MainActivity.countMaravillasDelMundo++
                "Selvas" -> MainActivity.countSelvas++
            }

            favoritoButton.visibility = View.GONE
            Toast.makeText(applicationContext, "Añadido a favoritos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerClima(pais: String) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$pais&appid=$apiKey&units=metric"
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val responseData = response.body?.string()
                if (responseData != null) {
                    val json = JSONObject(responseData)
                    val main = json.getJSONObject("main")
                    val temp = main.getDouble("temp")
                    val weatherDescription = json.getJSONArray("weather").getJSONObject(0).getString("description")

                    // Actualiza la UI en el hilo principal
                    runOnUiThread {
                        findViewById<TextView>(R.id.clima).text = "Temperatura: "+"$temp °C"
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
