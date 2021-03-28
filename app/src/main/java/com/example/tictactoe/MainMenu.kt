package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)
        Toast.makeText(applicationContext,"Welcome to Dota 2 Tic Tac Toe", Toast.LENGTH_LONG).show()
        val btnPVP = findViewById<Button>(R.id.btnPVP)
        btnPVP.setOnClickListener{
            val intentGoToMainActivity = Intent(this, PlayerSetup::class.java)
            startActivity(intentGoToMainActivity)
        }
        val btnPVC = findViewById<Button>(R.id.btnPVC)
        btnPVC.setOnClickListener{
            val intentGoToPlayerVsComputer = Intent(this, PlayerVsComputer::class.java)
            startActivity(intentGoToPlayerVsComputer)
        }
    }
}