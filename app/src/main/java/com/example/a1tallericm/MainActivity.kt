package com.example.a1tallericm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    companion object {
        val favoritos = mutableListOf<String>()  // Lista de destinos favoritos
        var countPlayas = 0
        var countMontanas = 0
        var countCiudadesHistoricas = 0
        var countMaravillasDelMundo = 0
        var countSelvas = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val explorarDestinosButton: Button = findViewById(R.id.explorarDestinos)
        val categoriasSpinner: Spinner = findViewById(R.id.categorias)
        explorarDestinosButton.setOnClickListener {
            val categoriaSeleccionada = categoriasSpinner.selectedItem.toString()
            val intent = Intent(this, Destinos::class.java)
            intent.putExtra("CategoriaSeleccionada", categoriaSeleccionada)
            startActivity(intent)
        }
        val favoritosButton: Button = findViewById(R.id.favoritos)
        favoritosButton.setOnClickListener {
            val intent = Intent(this, Favoritos::class.java)
            startActivity(intent)
        }
        val recomendacionButton: Button = findViewById(R.id.recomendaciones)
        recomendacionButton.setOnClickListener{
            recomendaciones()
        }
    }

    private fun recomendaciones() {
        val intent = Intent(this, Recomendacion::class.java)
        val bundle = Bundle()
        val categoriaMasFrecuente = when {
            countPlayas >= countMontanas && countPlayas >= countCiudadesHistoricas && countPlayas >= countMaravillasDelMundo && countPlayas >= countSelvas -> "Playas"
            countMontanas >= countPlayas && countMontanas >= countCiudadesHistoricas && countMontanas >= countMaravillasDelMundo && countMontanas >= countSelvas -> "Montañas"
            countCiudadesHistoricas >= countPlayas && countCiudadesHistoricas >= countMontanas && countCiudadesHistoricas >= countMaravillasDelMundo && countCiudadesHistoricas >= countSelvas -> "Ciudades Históricas"
            countMaravillasDelMundo >= countPlayas && countMaravillasDelMundo >= countMontanas && countMaravillasDelMundo >= countCiudadesHistoricas && countMaravillasDelMundo >= countSelvas -> "Maravillas del Mundo"
            else -> "Selvas"
        }
        val destinosEnCategoria = favoritos.filter {
            obtenerDetallesDestino(it)?.getString("categoria") == categoriaMasFrecuente }
        val destinoRecomendado = if (destinosEnCategoria.isNotEmpty()) destinosEnCategoria.random() else "NA"
        val detallesDestinoRecomendado = obtenerDetallesDestino(destinoRecomendado)
        bundle.putString("Nombre", detallesDestinoRecomendado?.getString("nombre") ?: "NA")
        bundle.putString("Pais", detallesDestinoRecomendado?.getString("pais") ?: "NA")
        bundle.putString("Plan", detallesDestinoRecomendado?.getString("plan") ?: "NA")
        bundle.putInt("Precio", detallesDestinoRecomendado?.getInt("precio") ?: 0)
        bundle.putString("Categoria", detallesDestinoRecomendado?.getString("categoria") ?: "NA")
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    private fun obtenerDetallesDestino(nombre: String): JSONObject? {
        val json = JSONObject(loadJSONFromAsset())
        val destinosJson = json.getJSONArray("destinos")
        for (i in 0 until destinosJson.length()) {
            val jsonObject = destinosJson.getJSONObject(i)
            if (jsonObject.getString("nombre") == nombre) {
                return jsonObject
            }
        }
        return null
    }

    fun loadJSONFromAsset(): String? {
        var json: String? = null
        try {
            val isStream = assets.open("destinos.json")
            val size = isStream.available()
            val buffer = ByteArray(size)
            isStream.read(buffer)
            isStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return json
    }
}
