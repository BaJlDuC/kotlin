package com.example.quadraticequationsolution

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NumberFormatException
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    fun readCoefficients(view: View): Boolean {
        if (coefficient_a.getText().toString().equals("") ||
            coefficient_b.getText().toString().equals("") ||
            coefficient_c.getText().toString().equals("")) {
            val errorMessage =
                Toast.makeText(this, "Ошибка. Введите все 3 коэффициента", Toast.LENGTH_SHORT)
            errorMessage.show()
            return false
        }
        var a = 0.0
        var b = 0.0
        var c = 0.0
        try {
            a = coefficient_a.text.toString().toDouble()
            b = coefficient_b.text.toString().toDouble()
            c = coefficient_c.text.toString().toDouble()
        }
        catch (e: NumberFormatException) {
            val errorMessage = Toast.makeText(this, "Коэффициенты должны быть цифрами", Toast.LENGTH_SHORT)
            errorMessage.show()
            return false
        }
        if (a == 0.0) {
            val errorMessage = Toast.makeText(this, "Коэффициент 'a' не может быть равен 0", Toast.LENGTH_SHORT)
            errorMessage.show()
            return false
        }
        else {
            val message = Toast.makeText(this, "Данные корректны. Начинаю считать ...", Toast.LENGTH_SHORT)
            message.show()
            toSolution(a, b, c)
            return true
        }
    }
    fun toSolution (a: Double, b: Double, c: Double) {
        var discriminant = b * b - 4 * a * c
        // добавить обработку 0
        var x1 = (- b + sqrt(discriminant)) / 2 * a
        var x2 = (- b - sqrt(discriminant)) / 2 * a
        val solutionIntent = Intent(this, solutionActivity::class.java)
        solutionIntent.putExtra(solutionActivity.X1, x1)
        solutionIntent.putExtra(solutionActivity.X2, x2)
        solutionIntent.putExtra(solutionActivity.DISCRIMINANT, discriminant)
        startActivity(solutionIntent)
    }
}
