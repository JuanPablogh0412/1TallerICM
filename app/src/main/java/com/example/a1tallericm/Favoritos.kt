package com.example.a1tallericm

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Favoritos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favoritos)

        val listView: ListView = findViewById(R.id.listViewFavoritos)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, MainActivity.favoritos)
        listView.adapter = adapter

    }
}