package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class PlayerSetup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_setup)
        val btnPVPAccept = findViewById<Button>(R.id.btnPVPAccept)
        val text1 = findViewById<EditText>(R.id.editPlayer1Nickname)
        val text2 = findViewById<EditText>(R.id.editPlayer2Nickname)

        // if text fields are not empty else loop activity
        btnPVPAccept.setOnClickListener{
            if (TextUtils.isEmpty(text1.text.toString()) || TextUtils.isEmpty(text2.text.toString())){
                Toast.makeText(this, "Please write your nicknames!", Toast.LENGTH_SHORT).show()
            } else {
                //loads text from PlayerSetup activity to MainActivity activity
                val intent = Intent(this@PlayerSetup, MainActivity::class.java)
                val player1Nickname = text1.text.toString()
                intent.putExtra("player1Nickname",player1Nickname)
                val player2Nickname = text2.text.toString()
                intent.putExtra("player2Nickname",player2Nickname)
                //val intentGoToPlayerSetup = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}