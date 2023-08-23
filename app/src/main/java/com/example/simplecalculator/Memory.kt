package com.example.simplecalculator

import android.os.Parcel
import android.os.Parcelable

class Memory() : Parcelable {

    private var digitalCollector: String = ""
    private var digitalMemory: String = ""
    private var operationSign: Char = ' '
    private var decimalClicked: Boolean = false
    private var screenText: String = ""

    constructor(parcel: Parcel) : this() {
        digitalCollector = parcel.readString().toString()
        digitalMemory = parcel.readString().toString()
        operationSign = parcel.readInt().toChar()
        decimalClicked = parcel.readByte() != 0.toByte()
        screenText = parcel.readString().toString()
    }

    fun getCollector(): String {
        return digitalCollector
    }

    fun setCollector(value: String) {
        digitalCollector = value
    }

    fun getMemory(): String {
        return digitalMemory
    }

    fun setMemory(value: String) {
        digitalMemory = value
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

    fun getScreenText(): String {
        return screenText
    }

    fun setScreenText(value: String) {
        screenText = value
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(digitalCollector)
        parcel.writeString(digitalMemory)
        parcel.writeInt(operationSign.code)
        parcel.writeByte(if (decimalClicked) 1 else 0)
        parcel.writeString(screenText)
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
