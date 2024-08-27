package com.example.a1tallericm

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Recomendacion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recomendacion)

        val bundle = intent.getBundleExtra("bundle")
        val nombre = bundle?.getString("Nombre")
        val pais = bundle?.getString("Pais")
        val plan = bundle?.getString("Plan")
        val precio = bundle?.getInt("Precio")
        val categoria = bundle?.getString("Categoria")

        findViewById<TextView>(R.id.Nombre).text = nombre
        findViewById<TextView>(R.id.pais).text = pais
        findViewById<TextView>(R.id.categoria).text = categoria
        findViewById<TextView>(R.id.plan).text = plan
        findViewById<TextView>(R.id.precio).text = "USD "+precio.toString()

    }
}