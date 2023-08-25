package com.example.simplecalculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class Operations {

    companion object {

        private const val zero: String = "0"
        private const val zeroDot: String = "0."
        private const val empty: String = ""
        private var del: String = ""

        fun digitButtonClick(memory: Memory, buttonText: String) {
            // Если было нажата DEL после нажатия знака операции. Сделано что мы менялось число, уже введенное
            if (memory.getMemory() == memory.getScreenText()
                && memory.getDelClicked()
                && memory.getCollector() == empty
            ) {
                memory.setMemory(memory.getMemory() + buttonText)
                memory.setScreenText(memory.getMemory())
            } else {
                //ЭТО ДЛЯ ТОГО ЧТО БЫ ПРИ ОТОБРАЖЕНИИ НУЛЯ БЕЗ ТОЧКИ ОН НЕ ПИСАЛСЯ ПЕРВЫМ
                if ((memory.getCollector() != zeroDot) && (memory.getCollector() == zero)) {
                    memory.setCollector(empty)
                }
                // СОБИРАЕМ НОВОЕ ЧИСЛО
                memory.setCollector(memory.getCollector() + buttonText)
                memory.setScreenText(memory.getCollector())
            }
        }

        fun operationButtonClick(memory: Memory, buttonText: String) {
            // При любом нажатии на кнопки операции, меняем знак операции
            memory.setOperationSign(buttonText.single())
            // Проверка если нет никаких данных, то просто поставить знак на малом экране
            if (memory.getScreenText() == zero || memory.getScreenText() == zeroDot) {
                memory.setSmallScreenText(memory.getOperationSign().toString())
                memory.setMemory(zero)
                // Обратобка второго нажатия. Когда нет второго операнда (тоесть коллектор пустой). Просто обновляем знак на экране
            } else if (memory.getMemory() == memory.getScreenText() && memory.getCollector()
                    .isEmpty()
            ) {
                memory.setSmallScreenText(memory.getMemory() + memory.getOperationSign())
                // Если до этого не вводили никакое число и мемори пуст, добавить в него коллектор, а коллектор обнулить.
            } else if ((memory.getMemory().isEmpty() || memory.getMemory() == zero)
                && memory.getCollector().isNotEmpty()
            ) {
                memory.setMemory(memory.getCollector())
                memory.setCollector(empty)
                memory.setSmallScreenText(memory.getMemory() + memory.getOperationSign())
                // При втором нажатии производит вычисления
            } else if (memory.getMemory().isNotEmpty() && memory.getCollector()
                    .isNotEmpty()) {
                val halfResult = memory.getMemory()
                memory.setMemory(calculateResult(memory))
                // Выводим результат на оба экрана
                memory.setScreenText(memory.getMemory())
                memory.setSmallScreenText(
                    halfResult
                            + memory.getOperationSign()
                            + memory.getCollector()
                            + "="
                )
            }

//            // Если нажать повторно кнопку операции, просто менять знак
//            if (memory.getOperationClicked()) {
//                memory.setSmallScreenText(memory.getScreenText() + memory.getOperationSign())
//
//                // Если кнопка = не была нажата (то есть это первое нажатие), копировать число в мемори и чистить коллектор.
//            } else if (!memory.getEqualClicked()) {
//                memory.setMemory(memory.getCollector())
//                memory.setCollector(empty)
//                memory.setSmallScreenText(memory.getMemory() + memory.getOperationSign())
//
//                // Если была нажата кнопка =, то просто вывести на малом экране мемори и знак.
//            } else if (memory.getEqualClicked()) {
//                memory.setSmallScreenText(memory.getMemory() + memory.getOperationSign())
//            }
            // Сбрасываем флаги точки и удаления
            memory.setDelClicked(false)
            memory.setDecimalClicked(false)

            // Устанавливаем флаг нажатой кнопки операций
            memory.setOperationClicked(true)

            // Выводим на экран мемори при любом нажатии на кнопку операции
            memory.setScreenText(memory.getMemory())
        }

        fun equalButtonClick(memory: Memory) {
            // Проверка что есть оба операнда. Если нет, ничего не делать
            if (memory.getMemory().isNotEmpty() && memory.getCollector().isNotEmpty()) {
                // Считаем вычисления и копируем результат в memory.
                memory.setMemory(calculateResult(memory))
                // Выводим результат на оба экрана
                memory.setScreenText(memory.getMemory())
                memory.setSmallScreenText(
                    memory.getMemory()
                            + memory.getOperationSign()
                            + memory.getCollector()
                            + "="
                )
            }
            // СБрасываем флаг точки, что бы можно было сразу набрать .0
            memory.setDecimalClicked(false)
            // Устанавливаем флаг нажатия на =
            memory.setEqualClicked(true)
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
            if (memory.getCollector().contains(".")) memory.setDecimalClicked(true)
            else memory.setDecimalClicked(false)
            memory.setDelClicked(true)
            memory.setScreenText(del)
        }

        fun cleanButtonClick(memory: Memory) {
            memory.setCollector("")
            memory.setMemory("")
            memory.setOperationSign(' ')
            memory.setScreenText("0")
            memory.setDecimalClicked(false)
            memory.setEqualClicked(false)
            memory.setOperationClicked(false)
            memory.setSmallScreenText("")
        }

        private fun calculateResult(memory: Memory): String {
            val operand1 = if (memory.getMemory().isNotEmpty())
                memory.getMemory().toDouble() else 0.0
            val operand2 = if (memory.getCollector().isNotEmpty())
                memory.getCollector().toDouble() else 0.0

            return when (memory.getOperationSign()) {
                '+' -> formatResult(operand1 + operand2)
                '-' -> formatResult(operand1 - operand2)
                '*' -> formatResult(operand1 * operand2)
                '/' -> formatResult(divide(operand1, operand2))

                else -> ""
            }
        }

        private fun divide(operand1: Double, operand2: Double): Double {
            return operand1 / operand2
        }

        private fun formatResult(result: Double): String {
            val formatted: String
            return if (result % 1.0 == 0.0) {
                formatted = String.format("%.0f", result)
                formatted
            } else {
                formatted = String.format("%.5f", result)
                formatted
            }
        }

        fun copyScreenToClipboard(context: Context, text: String) {
            val clipboard =
                context.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Calculator Result", removeLastSymbolIfMatch(text))
            clipboard.setPrimaryClip(clip)
        }

        fun removeLastSymbolIfMatch(input: String): String {
            val symbolsToRemove = setOf('+', '-', '*', '/')
            val lastSymbol = input.lastOrNull()

            return if (lastSymbol in symbolsToRemove) {
                input.dropLast(1)
            } else {
                input
            }
        }
    }
}