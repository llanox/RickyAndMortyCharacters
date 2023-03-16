package co.gabriel.rickyandmorty.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.gabriel.rickyandmorty.R
import co.gabriel.rickyandmorty.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}