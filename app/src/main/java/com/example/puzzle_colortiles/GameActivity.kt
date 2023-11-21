package com.example.puzzle_colortiles

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class Coord(val x: Int, val y: Int)

class GameActivity : AppCompatActivity() {

    private lateinit var tiles: Array<Array<View>>
    private var colorTitles = 0
    private var countColumn = 4
    private var countRow = 4
    private var answerBuf = ArrayList<String>()
    private var helpTitleColor = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tiles = Array(countColumn) { x ->
            Array(countRow) { y ->
                findViewById<View>(resources.getIdentifier("t$x$y", "id", packageName))
            }
        }
        initField()
        Toast.makeText(this, "Start. You can win!!!!", Toast.LENGTH_SHORT).show()
    }

    private fun getCoordFromString(s: String): Coord {
        return Coord(s[0] - '0', s[1] - '0')
    }

    private fun changeColor(view: View) {
        val brightColor = resources.getColor(R.color.bright)
        val darkColor = resources.getColor(R.color.dark)
        val drawable = view.background as ColorDrawable
        if (drawable.color == brightColor) {
            view.setBackgroundColor(darkColor)
            colorTitles += 1
        } else {
            view.setBackgroundColor(brightColor)
            colorTitles -= 1
        }
    }

    fun onClick(v: View) {
        val targetColor = resources.getColor(R.color.targetHelp)
        val drawable = v.background as ColorDrawable
        val brightColor = resources.getColor(R.color.bright)
        val darkColor = resources.getColor(R.color.dark)
        if (drawable.color == targetColor) {
            if (helpTitleColor) v.setBackgroundColor(darkColor)
            else v.setBackgroundColor(brightColor)
        }
        val coord = getCoordFromString(v.tag.toString())
        changeColor(v)
        if (v.tag.toString() in answerBuf) {
            answerBuf.remove(v.tag.toString())
        } else answerBuf.add(v.tag.toString())
        for (i in 0..3) {
            changeColor(tiles[coord.x][i])
            changeColor(tiles[i][coord.y])
        }
        checkVictory()
        if (answerBuf.isEmpty()) fillAnswerBuf()
    }

    private fun checkVictory() {
        if (colorTitles == countColumn * countRow || colorTitles == 0) {
            Toast.makeText(this, "You win!!!!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            lifecycleScope.launch {
                delay(500)
                startActivity(intent)
            }
        }
    }

    private fun initField() {
        for (colum in tiles) {
            for (cell in colum) {
                if (Random.nextBoolean()) {
                    changeColor(cell)
                    answerBuf.add(cell.tag.toString())
                }
            }
        }

    }

    private fun fillAnswerBuf() {
        val darkColor = resources.getColor(R.color.dark)
        for (colum in tiles) {
            for (cell in colum) {
                val drawable = cell.background as ColorDrawable
                if (drawable.color == darkColor) {
                    answerBuf.add(cell.tag.toString())
                }
            }
        }
    }


    fun help(v: View) {
        if (answerBuf.isNotEmpty()) {
            val targetColor = resources.getColor(R.color.targetHelp)
            val cord = getCoordFromString(answerBuf[0])
            val drawable = tiles[cord.x][cord.y].background as ColorDrawable
            val darkColor = resources.getColor(R.color.dark)
            if (drawable.color != targetColor)
                helpTitleColor = (drawable.color == darkColor)
            tiles[cord.x][cord.y].setBackgroundColor(targetColor)
        }
    }

}

