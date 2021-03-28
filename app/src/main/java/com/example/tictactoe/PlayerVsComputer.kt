package com.example.tictactoe

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat

class PlayerVsComputer : AppCompatActivity() {
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

    private val player1Set = mutableSetOf<Int>()
    private val player2Set = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_vs_computer)

        //MainMenu Button interaction
        val btnMainMenu = findViewById<Button>(R.id.btnMainMenu)
        btnMainMenu.setOnClickListener{
            val intentGoToPlayerSetup = Intent(this, MainMenu::class.java)
            startActivity(intentGoToPlayerSetup)
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
        //gives unique ID to every Button
        var cellID = 0
        when(btn.id){
            R.id.btn00->cellID = 1
            R.id.btn01->cellID = 2
            R.id.btn02->cellID = 3
            R.id.btn10->cellID = 4
            R.id.btn11->cellID = 5
            R.id.btn12->cellID = 6
            R.id.btn20->cellID = 7
            R.id.btn21->cellID = 8
            R.id.btn22->cellID = 9
        }
        //checks if button is empty
        if(btn.drawable != null) return
        //if this is player 1 turn
        if(player1Turn) {
            btn.setImageResource(R.drawable.shadow_fiend)
            player1Set.add(cellID)
        }
        else{
            // computer moves based on random number
            var found = false
            while(found == false) {
                var randomNumber = (1..9).random()
                cellID = randomNumber
                // checks if sets doesn't contain this cellID, so it wasn't yet taken
                if (!player1Set.contains(cellID) && !player2Set.contains(cellID)) {
                    player2Set.add(cellID)
                    when(cellID){
                        1->buttons[0][0].setImageResource(R.drawable.rubick)
                        2->buttons[0][1].setImageResource(R.drawable.rubick)
                        3->buttons[0][2].setImageResource(R.drawable.rubick)
                        4->buttons[1][0].setImageResource(R.drawable.rubick)
                        5->buttons[1][1].setImageResource(R.drawable.rubick)
                        6->buttons[1][2].setImageResource(R.drawable.rubick)
                        7->buttons[2][0].setImageResource(R.drawable.rubick)
                        8->buttons[2][1].setImageResource(R.drawable.rubick)
                        9->buttons[2][2].setImageResource(R.drawable.rubick)
                    }
                    found = true
                }
            }
        }
        //disable button after clicking
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
        val drwShadowFiend: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.shadow_fiend, null)
        val drwRubick: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.rubick, null)

        return when(drw?.constantState){
            drwShadowFiend?.constantState -> 'x'
            drwRubick?.constantState -> 'o'
            else -> null
        }
    }

    private fun win(player: Int) {
        if (player == 1){
            player1Points++
            Toast.makeText(applicationContext,"Player $player Won!", Toast.LENGTH_SHORT).show()
        }else{
            player2Points++
            Toast.makeText(applicationContext,"Computer Won!", Toast.LENGTH_SHORT).show()
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
        player1Set.removeAll(1..9)
        player2Set.removeAll(1..9)
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