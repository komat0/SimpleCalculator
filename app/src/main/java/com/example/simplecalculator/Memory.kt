package com.example.simplecalculator

import android.os.Parcel
import android.os.Parcelable

class Memory() : Parcelable {

    private var digitCollector: String = ""
    private var digitMemory: String = ""
    private var operationSign: Char = ' '
    private var decimalClicked: Boolean = false
    private var equalClicked: Boolean = false
    private var operationSignClicked: Boolean = false
    private var delClicked: Boolean = false
    private var minusSignClicked: Boolean = false
    private var screenText: String = ""
    private var smallScreenText: String = ""


    constructor(parcel: Parcel) : this() {
        digitCollector = parcel.readString().toString()
        digitMemory = parcel.readString().toString()
        operationSign = parcel.readInt().toChar()
        decimalClicked = parcel.readByte() != 0.toByte()
        equalClicked = parcel.readByte() != 0.toByte()
        operationSignClicked = parcel.readByte() != 0.toByte()
        delClicked = parcel.readByte() != 0.toByte()
        minusSignClicked = parcel.readByte() != 0.toByte()
        screenText = parcel.readString().toString()
        smallScreenText = parcel.readString().toString()
    }

    fun getCollector(): String {
        return digitCollector
    }

    fun setCollector(value: String) {
        digitCollector = value
    }

    fun getMemory(): String {
        return digitMemory
    }

    fun setMemory(value: String) {
        digitMemory = value
    }

    fun getOperationSign(): Char {
        return operationSign
    }

    fun setOperationSign(value: Char) {
        operationSign = value
    }

    fun getDecimalClicked(): Boolean {
        return decimalClicked
    }

    fun setDecimalClicked(clicked: Boolean) {
        decimalClicked = clicked
    }
    fun getEqualClicked(): Boolean {
        return equalClicked
    }

    fun setEqualClicked(clicked: Boolean) {
        equalClicked = clicked
    }
    fun getOperationClicked(): Boolean {
        return operationSignClicked
    }

    fun setOperationClicked(clicked: Boolean) {
        operationSignClicked = clicked
    }
    fun getDelClicked(): Boolean {
        return delClicked
    }

    fun setDelClicked(clicked: Boolean) {
        delClicked = clicked
    }
    fun getMinusSignClicked(): Boolean {
        return minusSignClicked
    }

    fun setMinusSignClicked(clicked: Boolean) {
        minusSignClicked = clicked
    }

    fun getScreenText(): String {
        return screenText
    }

    fun setScreenText(value: String) {
        screenText = value
    }
    fun getSmallScreenText(): String {
        return smallScreenText
    }

    fun setSmallScreenText(value: String) {
        smallScreenText = value
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(digitCollector)
        parcel.writeString(digitMemory)
        parcel.writeInt(operationSign.code)
        parcel.writeByte(if (decimalClicked) 1 else 0)
        parcel.writeByte(if (equalClicked) 1 else 0)
        parcel.writeByte(if (operationSignClicked) 1 else 0)
        parcel.writeByte(if (minusSignClicked) 1 else 0)

        parcel.writeString(screenText)
        parcel.writeString(smallScreenText)
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
