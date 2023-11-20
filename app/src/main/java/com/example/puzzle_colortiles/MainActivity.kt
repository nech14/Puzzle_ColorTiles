package com.example.puzzle_colortiles

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlin.random.Random

data class Coord(val x: Int, val y: Int)

class MainActivity : AppCompatActivity() {
    lateinit var tiles: Array<Array<View>>
    var colorTitles = 1
    var countColumn = 4
    var countRow = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tiles = Array(countColumn) { x ->
            Array(countRow) { y ->
                findViewById<View>(resources.getIdentifier("t$x$y", "id", packageName))
            }
        }
        initField()
        Toast.makeText(this, "Start. You can win!!!!", Toast.LENGTH_SHORT).show()
    }

    fun getCoordFromString(s: String): Coord {
        return Coord(s[0]-'0',s[1]-'0')
    }

    fun changeColor(view: View) {
        val brightColor = resources.getColor(R.color.bright)
        val darkColor = resources.getColor(R.color.dark)
        val drawable = view.background as ColorDrawable
        if (drawable.color ==brightColor ) {
            view.setBackgroundColor(darkColor)
            colorTitles += 1
        } else {
            view.setBackgroundColor(brightColor)
            colorTitles -= 1
        }
    }

    fun onClick(v: View) {
        val coord = getCoordFromString(v.getTag().toString())
        changeColor(v)

        for (i in 0..3) {
            changeColor(tiles[coord.x][i])
            changeColor(tiles[i][coord.y])
        }
        checkVictory()
    }

    fun checkVictory() {
        if(colorTitles == countColumn*countRow || colorTitles==0){
            Toast.makeText(this, "You win!!!!", Toast.LENGTH_SHORT).show()
        }
    }

    fun initField() {

        for(colum in tiles){
            for(cell in colum){
                if (Random.nextBoolean()){
                    changeColor(cell)
                }
            }
        }

        // TODO: заполнение поля случайными плитками (тёмный/светлый)

    }

}