package com.example.geekhublesson2recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geekhublesson2recyclerview.databinding.ActivityMainBinding
import ua.motionman.recyclerviewlecture.helper.messageList


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val baseAdapter = RecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initialiseAdapter()

        baseAdapter.messageList = messageList
    }

    fun initialiseAdapter() {
        binding.recyclerView.apply {
            adapter = baseAdapter
        }
    }
}