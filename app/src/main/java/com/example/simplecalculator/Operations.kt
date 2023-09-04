package com.example.simplecalculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.util.Locale

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
                //ЭТО ЧТО БЫ ПРИ ОТОБРАЖЕНИИ НУЛЯ БЕЗ ТОЧКИ ОН НЕ ПИСАЛСЯ ПЕРВЫМ
                if (memory.getCollector() == zero) {
                    memory.setCollector(empty)
                }
                // Условия для сбора числа без сброса колелктора после вычислений и нажатия на операцию
                else if (memory.getMemory().isNotEmpty()
                    && (memory.getCollector().isEmpty()
                            || memory.getCollector() == zeroDot)
                    && memory.getEqualClicked()
                ) {
                    memory.setEqualClicked(false)
                }
                // Если были сделаны вычисления, то обнуляем строку что бы можно было собирать новое число
                else if (memory.getMemory().isNotEmpty()
                    && memory.getCollector().isNotEmpty()
                    && memory.getEqualClicked()
                ) {
                    memory.setMemory(empty)
                    memory.setCollector(empty)
                    memory.setEqualClicked(false)
                }
                // СОБИРАЕМ НОВОЕ ЧИСЛО
                memory.setCollector(memory.getCollector() + buttonText)
                memory.setScreenText(memory.getCollector())
            }
        }

        fun operationButtonClick(memory: Memory, buttonText: String) {
            // После нажатия на равно, при нажатии на операцию, обнуляется коллектор. Что бы после равно продолжить вычисления
            if (memory.getMemory().isNotEmpty()
                && memory.getCollector().isNotEmpty()
                && memory.getEqualClicked()
            ) {
                memory.setOperationSign(buttonText.single())
                memory.setCollector(empty)
//                memory.setEqualClicked(false)
                memory.setSmallScreenText(memory.getMemory() + memory.getOperationSign())
            }
            // При втором нажатии производит вычисления и чистит коллектор
            else if (memory.getMemory().isNotEmpty() && memory.getCollector().isNotEmpty()) {
                memory.setMemory(calculateResult(memory))
                memory.setCollector(empty)
                memory.setOperationSign(buttonText.single())
                memory.setSmallScreenText(memory.getMemory() + memory.getOperationSign())
            }
            // Если до этого не вводили никакое число и мемори пуст, добавить в него коллектор, а коллектор чистим.
            else if (memory.getMemory().isEmpty() && memory.getCollector().isNotEmpty()) {
                memory.setOperationSign(buttonText.single())
                memory.setMemory(memory.getCollector())
                memory.setCollector(empty)
                memory.setSmallScreenText(memory.getMemory() + memory.getOperationSign())
            }
            // Если после нажатия на операцию нажать кнопку операций просто поменять знак.
            else if (memory.getMemory().isNotEmpty() && memory.getCollector().isEmpty()) {
                memory.setOperationSign(buttonText.single())
                memory.setSmallScreenText(memory.getMemory() + memory.getOperationSign())
            }
            // Если нажаты первыми, ничего не делать.
            else if (memory.getMemory().isEmpty() && memory.getCollector().isEmpty()) {
                memory.setSmallScreenText(memory.getOperationSign().toString())
                memory.setScreenText(zero)
            }

            // Сбрасываем флаги точки, удаления и знака минус
            memory.setDelClicked(false)
            memory.setDecimalClicked(false)
            memory.setMinusSignClicked(false)
        }

        fun equalButtonClick(memory: Memory) {
            // Проверка что есть оба операнда. Если нет, ничего не делать
            if (memory.getMemory().isNotEmpty() && memory.getCollector().isNotEmpty()) {
                // Сначала выводим малый экран, так как после вычислений мемори изменится.
                memory.setSmallScreenText(
                    memory.getMemory()
                            + memory.getOperationSign()
                            + memory.getCollector()
                            + "="
                )
                // Считаем вычисления и копируем результат в memory.
                memory.setMemory(calculateResult(memory))
                // Выводим результат на экран
                memory.setScreenText(memory.getMemory())
            }
            // Сбрасываем флаг точки, что бы можно было сразу набрать .0
            memory.setDecimalClicked(false)
            // убираем флаг нажатого минуса
            memory.setMinusSignClicked(false)
            // Устанавливаем флаг нажатия на кнопку =
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
            memory.setSmallScreenText("")
            memory.setMinusSignClicked(false)

        }

        private fun calculateResult(memory: Memory): String {
            val operand1 = if (memory.getMemory().isNotEmpty())
                memory.getMemory().toDouble() else 0.0
            val operand2 = if (memory.getCollector().isNotEmpty())
                memory.getCollector().toDouble() else 0.0
            // Проверка деления на ноль
            if (memory.getOperationSign() == '÷' && operand2 == 0.0) {
                cleanButtonClick(memory)
                memory.setSmallScreenText("Don't try to divide by 0")
                return ""
            }

            return when (memory.getOperationSign()) {
                '+' -> formatResult(operand1 + operand2)
                '-' -> formatResult(operand1 - operand2)
                '×' -> formatResult(operand1 * operand2)
                '÷' -> formatResult(divide(operand1, operand2))

                else -> ""
            }
        }

        private fun divide(operand1: Double, operand2: Double): Double {
            return operand1 / operand2
        }

        private fun formatResult(result: Double): String {
            Locale.setDefault(Locale.US)
            val df = DecimalFormat("#.####")
            val formattedResult = if (result == 0.0) {
                "0"
            } else {
                df.format(result)
            }
            return formattedResult
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

        fun addMinus(memory: Memory) {
            val charToReplace = '-'
            val stringToReplace = memory.getCollector().replace(charToReplace.toString(), "")
            // Проверяем что коллектор и экран совпадают, значит идет сбор числа и что - не был нажат
            if (memory.getCollector() == memory.getScreenText()
                && !memory.getMinusSignClicked()
            ) {
                memory.setCollector("-" + memory.getCollector())
                memory.setScreenText(memory.getCollector())
                memory.setMinusSignClicked(true)
                // Если был нажат - удаляем флаг и удаляем - из строки.
            } else if (memory.getCollector() == memory.getScreenText() && memory.getMinusSignClicked()) {
                memory.setCollector(stringToReplace)
                memory.setScreenText(memory.getCollector())
                memory.setMinusSignClicked(false)
            }
        }
    }
}