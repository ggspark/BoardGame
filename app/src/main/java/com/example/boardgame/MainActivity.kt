package com.example.boardgame

import MainViewModel
import N
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridLayout: GridLayout = findViewById(R.id.board)
        gridLayout.columnCount = N

        val diceValue: TextView = findViewById(R.id.dice_value)
        val diceButton: Button = findViewById(R.id.dice_button)
        diceButton.setOnClickListener {
            viewModel.diceRolled()
        }

        viewModel.dice.observe(this, Observer {
            diceValue.text = it.toString()
        })

        viewModel.board.observe(this, Observer { array ->
            renderBoard(gridLayout, array)
        })
    }

    /**
     * Render N*N 2d board on screen based on single dimension array of N*N
     */
    private fun renderBoard(gridLayout: GridLayout, array: IntArray) {
        for (i in 0 until gridLayout.columnCount) {
            for (j in 0 until gridLayout.columnCount) {
                val textView = TextView(this)
                val pos = i * gridLayout.columnCount + j
                textView.text = array[pos].toString()
                if (viewModel.token == pos) { //Show green bg if token is on this position
                    textView.background = getDrawable(R.drawable.border_green)
                } else {
                    textView.background = getDrawable(R.drawable.border_gray)
                }
                textView.gravity = Gravity.CENTER
                val myGLP = GridLayout.LayoutParams()
                //Start from bottom row
                myGLP.rowSpec = GridLayout.spec(gridLayout.columnCount - i, 1, 1f)
                myGLP.columnSpec = GridLayout.spec(j, 1, 1f)
                myGLP.width = 0
                myGLP.height = 0
                gridLayout.addView(textView, myGLP)
            }
        }
    }
}
