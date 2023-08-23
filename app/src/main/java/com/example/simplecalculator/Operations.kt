package com.example.simplecalculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class Operations {

    companion object {

        private const val zero: String = "0"
        private const val zeroDot: String = "0."
        private const val empty: String = ""
        private var del: String = ""
        private var checkDel = "0"

        fun digitButtonClick(memory: Memory, buttonText: String) {
            // Check if sign button was taped. if yes change Memory without changes Collector
            if (memory.getMemory() == memory.getScreenText()
                && checkDel == "1"
                && memory.getCollector() == empty
            ) {
                memory.setMemory(memory.getMemory() + buttonText)
                memory.setScreenText(memory.getMemory())
            } else {
                // Check if equal button wasn't taped AND not ".0" in the Collector.
                // If yes collect new Collector
                if ((memory.getMemory() == memory.getScreenText()
                            && memory.getCollector() != zeroDot)
                    || (memory.getCollector() == zero)
                ) {
                    memory.setCollector(empty)
                }
                // Creating new number in Collector
                memory.setCollector(memory.getCollector() + buttonText)
                memory.setScreenText(memory.getCollector())
            }
        }

        fun operationButtonClick(memory: Memory, buttonText: String) {
            // Check if equals button wasn't taped. If yes, than write Collector into the Memory
            if (memory.getMemory() != memory.getScreenText()) {
                memory.setMemory(memory.getCollector())
            }
            // Clear Collector
            memory.setCollector(empty)
            // Set operation sign
            memory.setOperationSign(buttonText.single())
            memory.setDecimalClicked(false)
            // Show operation sign on the screen
            memory.setScreenText(memory.getMemory() + buttonText.single())
            checkDel = "0"
        }

        fun dotButtonClick(memory: Memory, buttonText: String) {
            if (!memory.getDecimalClicked()) {
                if (memory.getScreenText() == zero
                    || memory.getCollector() == empty
                    || memory.getMemory() == memory.getScreenText()
                ) {
                    memory.setCollector(zeroDot)
                } else {
                    memory.setCollector(memory.getCollector() + buttonText)
                }
                memory.setScreenText(memory.getCollector())
                memory.setDecimalClicked(true)
            }
        }

        fun equalsButtonClick(memory: Memory) {
            memory.setMemory(calculateResult(memory))
            memory.setScreenText(memory.getMemory())
            memory.setDecimalClicked(false)
        }

        fun deleteButtonClick(memory: Memory) {
            // Check if ScreenText is empty or only one digit
            if (memory.getScreenText().isEmpty()
                || memory.getScreenText().length == 1
            ) {
                memory.setCollector(empty)
                del = zero
                // Check if its collected number between signs
            } else if (memory.getCollector() != empty
                && memory.getScreenText() == memory.getCollector()
            ) {
                del = memory.getCollector().dropLast(1)
                memory.setCollector(del)
                // Check if sign was taped
            } else if (memory.getCollector() == empty
                && memory.getMemory() != empty
            ) {
                del = memory.getMemory().dropLast(1)
                memory.setMemory(del)
                // Check if equal button was taped and do nothing
            } else if (memory.getMemory() == memory.getScreenText()
                && memory.getCollector() != empty
            ) {
                del = memory.getMemory()
            }
            checkDel = "1"
            memory.setScreenText(del)
        }

        fun calculateResult(memory: Memory): String {
            val operand1 = if (memory.getMemory().isNotEmpty())
                memory.getMemory().toDouble() else 0.0
            val operand2 = if (memory.getCollector().isNotEmpty())
                memory.getCollector().toDouble() else 0.0

            return when (memory.getOperationSign()) {
                '+' -> formatResult(operand1 + operand2)
                '-' -> formatResult(operand1 - operand2)
                '×' -> formatResult(operand1 * operand2)
                '÷' -> formatResult(divide(operand1, operand2))

                else -> ""
            }
        }

        fun divide(operand1: Double, operand2: Double): Double {
            val result = operand1 / operand2
            return result
        }

        fun isInteger(number: Double): Boolean {
            return number % 1 == 0.0
        }

        fun formatResult(result: Double): String {
            return if (isInteger(result)) {
                result.toInt().toString()
            } else {
                result.toString()
            }
        }

        fun copyToClipboard(context: Context, text: String) {
            val clipboard =
                context.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Calculator Result", text)
            clipboard.setPrimaryClip(clip)
        }

        fun cleanCalculator(memory: Memory) {
            memory.setCollector("")
            memory.setMemory("")
            memory.setOperationSign(' ')
            memory.setDecimalClicked(false)
            memory.setScreenText("0")
        }
    }
}