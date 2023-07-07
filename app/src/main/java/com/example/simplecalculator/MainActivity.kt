package com.example.simplecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    private lateinit var calculatorScreenText: TextView
    private lateinit var actionButtons: List<Button>
    private lateinit var digitalButtons: List<Button>
    private lateinit var buttonDivision: Button
    private lateinit var buttonMulti: Button
    private lateinit var buttonPlus: Button
    private lateinit var buttonMinus: Button
    private lateinit var buttonDot: Button
    private lateinit var buttonEquals: Button
    private lateinit var buttonClear: Button
    private lateinit var darkModeSwitcher: SwitchMaterial
    private var digitalCollector: String = ""
    private var digitalMemory: String = ""
    private var actionButtonSign: Char = ' '
    private var decimalClicked: Boolean = false
    private var textViewValueCopyByTap = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        buttonDivision = findViewById(R.id.buttonDivision)
        buttonMulti = findViewById(R.id.buttonMulti)
        buttonPlus = findViewById(R.id.buttonPlus)
        buttonMinus = findViewById(R.id.buttonMinus)
        buttonDot = findViewById(R.id.buttonDot)
        buttonEquals = findViewById(R.id.buttonEquals)
        buttonClear = findViewById(R.id.buttonClear)

        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)

        calculatorScreenText = findViewById(R.id.calculatorScreenText)
        darkModeSwitcher = findViewById(R.id.darkModeSwitch)

        digitalButtons = listOf(
            button0, button1, button2, button3, button4,
            button5, button6, button7, button8, button9
        )
        actionButtons = listOf(
            buttonDivision, buttonMulti, buttonPlus, buttonMinus
        )
    }

    fun onClickAnyButton(view: View) {
        val buttonText = (view as Button).text.toString()

        when (view) {
            in digitalButtons -> {
                if (actionButtonSign == '=' && digitalCollector == "0.") {
                    clearCalculator()
                } else if (actionButtonSign == '=') {
                    clearCalculator()
                }
                digitalCollector += buttonText
                calculatorScreenText.text = digitalCollector
            }

            in actionButtons -> {
                digitalMemory = digitalCollector
                digitalCollector = ""
                actionButtonSign = buttonText.single()
                decimalClicked = false
            }

            buttonDot -> {
                if (!decimalClicked) {
                    if (actionButtonSign == '=') {
                        clearCalculator()
                    }
                    if (digitalCollector.isEmpty()) {
                        digitalCollector = "0."
                    } else {
                        digitalCollector += "."
                    }
                    calculatorScreenText.text = digitalCollector
                    decimalClicked = true
                }
            }

            buttonEquals -> {
                val result = calculateResult()
                calculatorScreenText.text = result
                digitalCollector = result
                actionButtonSign = '='
                decimalClicked = false
            }

            buttonClear -> {
                clearCalculator()
            }
        }
    }

    private fun clearCalculator() {
        digitalMemory = ""
        digitalCollector = ""
        calculatorScreenText.text = "0"
        actionButtonSign = ' '
        decimalClicked = false
    }

    private fun calculateResult(): String {
        val operand1 = digitalMemory.toDouble()
        val operand2 = digitalCollector.toDouble()
        val result: Double = when (actionButtonSign) {
            '+' -> operand1 + operand2
            '-' -> operand1 - operand2
            '×' -> operand1 * operand2
            '÷' -> if (operand2 != 0.0) operand1 / operand2
            else {
                Toast.makeText(
                    applicationContext,
                    "Не пытайтесь делить на ноль.",
                    Toast.LENGTH_SHORT
                ).show()
                0.0
            }

            else -> 0.0
        }
        return if (isInteger(result)) {
            result.toInt().toString()
        } else {
            String.format("%.2f", result)
        }
    }

    private fun isInteger(number: Double): Boolean {
        return number % 1 == 0.0
    }

    fun onClickSwitcher(view: View) {
        if (darkModeSwitcher.isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Toast.makeText(
                applicationContext,
                "Ночной режим Активирован!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Toast.makeText(
                applicationContext,
                "Ночной режим Деактивирован!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun onClickTextView(view: View) {
        textViewValueCopyByTap = calculatorScreenText.text.toString()
        if (textViewValueCopyByTap.isNotEmpty()) {
            Toast.makeText(
                applicationContext,
                "В память скопировано значение: $textViewValueCopyByTap",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}