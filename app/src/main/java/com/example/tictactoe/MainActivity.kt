package com.example.tictactoe

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat

class MainActivity : AppCompatActivity() {
    //lateinit use when define variable but cant initialize it yet
    lateinit var buttons: Array<Array<ImageButton>>
    lateinit var textViewPlayer1: TextView
    lateinit var textViewPlayer2: TextView

    //player1Turn keeps track of players turn
    //roundCount total game rounds
    private var player1Turn: Boolean = true
    private var roundCount: Int = 0

    //player1Points and player2Points are players scores
    private var player1Points: Int = 0
    private var player2Points: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //saves nicknames from PlayerSetup activity
        val player1Nickname = intent.getStringExtra("player1Nickname")
        val setPlayer1Nickname = findViewById<TextView>(R.id.player1Nickname)
        setPlayer1Nickname.text = player1Nickname

        val player2Nickname = intent.getStringExtra("player2Nickname")
        val setPlayer2Nickname = findViewById<TextView>(R.id.player2Nickname)
        setPlayer2Nickname.text = player2Nickname

        val btnMainMenu = findViewById<Button>(R.id.btnMainMenu)
        btnMainMenu.setOnClickListener{
            val intentGoToMainMenu = Intent(this, MainMenu::class.java)
            startActivity(intentGoToMainMenu)
        }

        //initialize textViews of players by id
        textViewPlayer1 = findViewById(R.id.player1TextView)
        textViewPlayer2 = findViewById(R.id.player2TextView)
        buttons = Array(3){r->
            Array(3){c->
                initButtons(r,c)
            }
        }
        //initialize reset button by id
        val btnReset: Button = findViewById(R.id.btnReset)
        // set on click listener and displaying small message when clicks on button
        btnReset.setOnClickListener{
            player1Points = 0
            player2Points = 0
            updateScore()
            clearBoard()
        }
    }

    private fun initButtons(r:Int,c:Int): ImageButton {
        // getting reference to the button
        val btn: ImageButton =
                findViewById(resources.getIdentifier("btn$r$c","id", packageName))
        // set on click listener and makes action when clicked
        btn.setOnClickListener{
            onBtnClick(btn)}
        return btn
    }

    //when button is clicked logic
    private fun onBtnClick(btn: ImageButton) {
        //checks if button is empty
        if(btn.drawable != null) return
        //if this is player 1 turn
        if(player1Turn){
            btn.setImageResource(R.drawable.pudge)
        } else {
            btn.setImageResource(R.drawable.necr)
        }
        roundCount++
        //win, draw, lose logic
        if(checkForWin()){
            if(player1Turn)win(1) else win(2)
        } else if (roundCount == 9){
            draw()
        } else{
            //switch player turn
            player1Turn = !player1Turn
        }
    }

    //function checks crossing lines
    private fun checkForWin(): Boolean {
        val fields =  Array(3){r->
            Array(3){c->
                getField(buttons[r][c])
            }
        }
        //loop through all rows
        for (i in 0..2){
            if(
                (fields[i][0] == fields[i][1])&&
                (fields[i][0] == fields[i][2])&&
                (fields[i][0] != null)
            ) return true
        }
        //loop through all columns
        for (i in 0..2){
            if(
                (fields[0][i] == fields[1][i])&&
                (fields[0][i] == fields[2][i])&&
                (fields[0][i] != null)
            ) return true
        }
        //loop through 1st diagonal
        if(
            (fields[0][2] == fields[1][1])&&
            (fields[0][2] == fields[2][0])&&
            (fields[0][2] != null)
        ) return true
        //loop through 2nd diagonal
        if(
            (fields[0][0] == fields[1][1])&&
            (fields[0][0] == fields[2][2])&&
            (fields[0][0] != null)
        ) return true
        return false
    }

    // Char? returns char or null
    // passing image in function and changing it to x or o, so game logic could check fields state
    private fun getField(btn: ImageButton): Char? {
        val drw: Drawable? = btn.drawable
        val drwPudge: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.pudge, null)
        val drwNecr: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.necr, null)

        return when(drw?.constantState){
            drwPudge?.constantState -> 'x'
            drwNecr?.constantState -> 'o'
            else -> null
        }
    }

    private fun win(player: Int) {
        if(player == 1){
            val player1Nickname = intent.getStringExtra("player1Nickname")
            //val textViewForPlayer1: TextView = findViewById<TextView>(R.id.player1Nickname)
            player1Points++
            Toast.makeText(applicationContext,"Player $player1Nickname Won!", Toast.LENGTH_SHORT).show()
        } else{
            val player2Nickname = intent.getStringExtra("player2Nickname")
            //val textViewForPlayer2: TextView = findViewById<TextView>(R.id.player2Nickname)
            player2Points++
            Toast.makeText(applicationContext,"Player $player2Nickname Won!", Toast.LENGTH_SHORT).show()
        }
        //updates players scores
        updateScore()

        //cleans board after game
        clearBoard()
    }

    private fun clearBoard() {
        for(i in 0..2){
            for(j in 0..2){
                buttons[i][j].setImageResource(0)
            }
        }
        roundCount = 0
        player1Turn = true
    }

    private fun updateScore() {
        textViewPlayer1.text = "$player1Points"
        textViewPlayer2.text = "$player2Points"
    }

    private fun draw() {
        Toast.makeText(applicationContext,"Match Draw!", Toast.LENGTH_SHORT).show()
        //cleans board after game
        clearBoard()
    }

}