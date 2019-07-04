package com.example.quadraticequationsolution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_solution.*

class solutionActivity : AppCompatActivity() {
    companion object {
        const val X1 = "x1"
        const val X2 = "x2"
        const val DISCRIMINANT = "discriminant"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solution)
        showSolution()
    }
    fun showSolution() {
        val discriminant = intent.getDoubleExtra(DISCRIMINANT, 0.0)
        if (discriminant < 0)
            textView.text = "Уравнение не имеет решения, так как дискриминант меньше 0 ($discriminant)"
        else {
            val solution1 = intent.getDoubleExtra(X1, 0.0)
            val solution2 = intent.getDoubleExtra(X2, 0.0)
            textView3.text = "1) X1 = $solution1"
            textView7.text = "2) X2 = $solution2"
        }
    }
}
