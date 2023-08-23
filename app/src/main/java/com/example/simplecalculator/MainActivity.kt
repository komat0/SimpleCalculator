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
    private val zero: String = "0"
    private val empty: String = ""
    private lateinit var memory: Memory
    private lateinit var screenText: TextView
    private lateinit var operationButtons: List<Button>
    private lateinit var digitButtons: List<Button>
    private lateinit var buttonDivision: Button
    private lateinit var buttonMulti: Button
    private lateinit var buttonPlus: Button
    private lateinit var buttonMinus: Button
    private lateinit var buttonDot: Button
    private lateinit var buttonEquals: Button
    private lateinit var buttonDel: Button
    private lateinit var buttonClean: Button
    private lateinit var darkModeSwitcher: SwitchMaterial
    private var textViewValueCopyByTap = empty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        buttonDivision = findViewById(R.id.buttonDivision)
        buttonMulti = findViewById(R.id.buttonMulti)
        buttonPlus = findViewById(R.id.buttonPlus)
        buttonMinus = findViewById(R.id.buttonMinus)
        buttonDot = findViewById(R.id.buttonDot)
        buttonEquals = findViewById(R.id.buttonEquals)
        buttonDel = findViewById(R.id.buttonDel)
        buttonClean = findViewById(R.id.buttonClear)

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

        screenText = findViewById(R.id.calculatorScreenText)

        darkModeSwitcher = findViewById(R.id.darkModeSwitch)

        digitButtons = listOf(
            button0, button1, button2, button3, button4,
            button5, button6, button7, button8, button9
        )
        operationButtons = listOf(
            buttonDivision, buttonMulti, buttonPlus, buttonMinus
        )

        if (savedInstanceState != null) {
            memory = savedInstanceState.getParcelable(MEMORY_KEY) ?: Memory()
            screenText.text =
                savedInstanceState.getString(CALCULATOR_SCREEN_TEXT_KEY, zero)
        } else {
            memory = Memory()
        }
    }

    companion object {
        private const val MEMORY_KEY = "memory_key"
        private const val CALCULATOR_SCREEN_TEXT_KEY = "calculator_screen_text_key"
    }

    fun onClickAnyButton(view: View) {
        val buttonText = (view as Button).text.toString()

        when (view) {
            in digitButtons -> {
                Operations.digitButtonClick(memory, buttonText)
                screenText.text = memory.getScreenText()
            }

            in operationButtons -> {
                Operations.operationButtonClick(memory, buttonText)
                screenText.text = memory.getScreenText()
            }

            buttonDot -> {
                Operations.dotButtonClick(memory, buttonText)
                screenText.text = memory.getCollector()
            }

            buttonEquals -> {
                Operations.equalsButtonClick(memory)
                screenText.text = memory.getScreenText()
            }

            buttonClean -> {
                Operations.cleanCalculator(memory)
                screenText.text = zero
            }

            buttonDel -> {
                Operations.deleteButtonClick(memory)
                screenText.text = memory.getScreenText()
            }
        }
    }

    fun onClickSwitcher(view: View) {
        if (darkModeSwitcher.isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Toast.makeText(
                applicationContext,
                "Dark theme activated!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Toast.makeText(
                applicationContext,
                "Dark theme deactivated!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun onClickTextView(view: View) {
        textViewValueCopyByTap = screenText.text.toString()
        if (textViewValueCopyByTap.isNotEmpty()) {
            Operations.copyToClipboard(applicationContext, textViewValueCopyByTap)
            Toast.makeText(
                applicationContext,
                "Value copied into memory: $textViewValueCopyByTap",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(MEMORY_KEY, memory)
        outState.putString(CALCULATOR_SCREEN_TEXT_KEY, screenText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        memory = savedInstanceState.getParcelable(MEMORY_KEY) ?: Memory()
        memory.setScreenText(
            savedInstanceState.getString(
                CALCULATOR_SCREEN_TEXT_KEY,
                memory.getScreenText()
            )
        )
    }
}