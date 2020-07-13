package com.example.boardgame

import MainViewModel
import N
import android.os.Bundle
import android.view.Gravity
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

        val gridLayout: GridLayout = this.findViewById(R.id.board)
        gridLayout.columnCount = N

        viewModel.array.observe(this, Observer { array ->
            renderBoard(gridLayout, array)
        })


    }

    private fun renderBoard(gridLayout: GridLayout, array: IntArray) {
        for (i in 0 until gridLayout.columnCount) {
            for (j in 0 until gridLayout.columnCount) {
                val textView = TextView(this)
                textView.text = array[i * gridLayout.columnCount + j].toString()
                textView.background = getDrawable(R.drawable.border)
                textView.gravity = Gravity.CENTER
                val myGLP = GridLayout.LayoutParams()
                myGLP.rowSpec = GridLayout.spec(i, 1, 1f)
                myGLP.columnSpec = GridLayout.spec(j, 1, 1f)
                myGLP.width = 0
                myGLP.height = 0
                gridLayout.addView(textView, myGLP)
            }
        }
    }
}
