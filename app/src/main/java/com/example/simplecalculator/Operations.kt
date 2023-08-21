package com.example.simplecalculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Operations {
    companion object {
        fun clearCalculator(memory: Memory) {
            memory.setDigitalCollector("")
            memory.setDigitalMemory("")
            memory.setActionButtonSign(' ')
            memory.setDecimalClicked(false)
            memory.setCalculatorScreenText("0")
        }

        fun divide(operand1: Double, operand2: Double, context: Context): String {
            checkDivisionByZero(operand2, context)

            val result = operand1 / operand2
            return formatResult(result)
        }

        fun calculateResult(memory: Memory, context: Context): String {
            val operand1 = if (memory.getDigitalMemory().isNotEmpty())
                memory.getDigitalMemory().toDouble() else 0.0
            val operand2 = if (memory.getDigitalCollector().isNotEmpty())
                memory.getDigitalCollector().toDouble() else 0.0

            return when (memory.getActionButtonSign()) {
                '+' -> formatResult(operand1 + operand2)
                '-' -> formatResult(operand1 - operand2)
                '×' -> formatResult(operand1 * operand2)
                '÷' -> divide(operand1, operand2, context)

                else -> "0"
            }
        }

        fun isInteger(number: Double): Boolean {
            return number % 1 == 0.0
        }

        fun zeroToaster(context: Context) {
            Toast.makeText(
                context,
                "Don't try to divide by zero",
                Toast.LENGTH_SHORT
            ).show()
        }

        fun checkDivisionByZero(operand2: Double, context: Context) {
            if (operand2 == 0.0) {
                zeroToaster(context)
            }
        }

        fun formatResult(result: Double): String {
            return if (isInteger(result)) {
                result.toInt().toString()
            } else {
                String.format("%.2f", result)
            }
        }

        fun copyToClipboard(context: Context, text: String) {
            val clipboard = context.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Calculator Result", text)
            clipboard.setPrimaryClip(clip)
        }
    }
}