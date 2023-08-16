package com.example.simplecalculator

import android.os.Parcel
import android.os.Parcelable

class Memory() : Parcelable {

    private var digitalCollector: String = ""
    private var digitalMemory: String = ""
    private var actionButtonSign: Char = ' '
    private var decimalClicked: Boolean = false
    private var calculatorScreenText: String = ""

    constructor(parcel: Parcel) : this() {
        digitalCollector = parcel.readString().toString()
        digitalMemory = parcel.readString().toString()
        actionButtonSign = parcel.readInt().toChar()
        decimalClicked = parcel.readByte() != 0.toByte()
        calculatorScreenText = parcel.readString().toString()
    }

    fun getDigitalCollector(): String {
        return digitalCollector
    }

    fun setDigitalCollector(value: String) {
        digitalCollector = value
    }

    fun getDigitalMemory(): String {
        return digitalMemory
    }

    fun setDigitalMemory(value: String) {
        digitalMemory = value
    }

    fun getActionButtonSign(): Char {
        return actionButtonSign
    }

    fun setActionButtonSign(value: Char) {
        actionButtonSign = value
    }

    fun getDecimalClicked(): Boolean {
        return decimalClicked
    }

    fun setDecimalClicked(clicked: Boolean) {
        decimalClicked = clicked
    }

    fun getCalculatorScreenText(): String {
        return calculatorScreenText
    }

    fun setCalculatorScreenText(value: String) {
        calculatorScreenText = value
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(digitalCollector)
        parcel.writeString(digitalMemory)
        parcel.writeInt(actionButtonSign.code)
        parcel.writeByte(if (decimalClicked) 1 else 0)
        parcel.writeString(calculatorScreenText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Memory> {
        override fun createFromParcel(parcel: Parcel): Memory {
            return Memory(parcel)
        }

        override fun newArray(size: Int): Array<Memory?> {
            return arrayOfNulls(size)
        }
    }
}
