package com.example.a1tallericm

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class Destinos : AppCompatActivity() {
    var destinos = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destinos)

        val categoriaSeleccionada = intent.getStringExtra("CategoriaSeleccionada").toString()
        llenarDestinos(categoriaSeleccionada)
        configurarListView()
    }

    private fun configurarListView() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, destinos)
        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, position, id ->
            abrirDetalleDestino(destinos[position])
        }
    }

    private fun abrirDetalleDestino(destinoSeleccionado: String) {
        val detallesDestino = obtenerDetallesDestino(destinoSeleccionado)
        detallesDestino?.let { destino ->
            val intent = Intent(this, DetalleDestino::class.java)
            val bundle = Bundle()
            bundle.putString("Nombre", destino.getString("nombre"))
            bundle.putString("Pais", destino.getString("pais"))
            bundle.putString("Plan", destino.getString("plan"))
            bundle.putInt("Precio", destino.getInt("precio"))
            bundle.putString("Categoria", destino.getString("categoria"))
            intent.putExtra("bundle", bundle)
            startActivity(intent)
        }
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
            val isStream: InputStream = assets.open("destinos.json")
            val size: Int = isStream.available()
            val buffer = ByteArray(size)
            isStream.read(buffer)
            isStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return json
    }

    private fun llenarDestinos(categoria: String) {
        val json = JSONObject(loadJSONFromAsset())
        val destinosJson = json.getJSONArray("destinos")
        for (i in 0 until destinosJson.length()) {
            val jsonObject = destinosJson.getJSONObject(i)
            if (jsonObject.getString("categoria") == categoria || categoria == "Todos") {
                destinos.add(jsonObject.getString("nombre"))
            }
        }
    }
}
