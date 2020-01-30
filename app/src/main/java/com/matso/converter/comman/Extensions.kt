package com.matso.converter.comman

import android.graphics.Paint
import android.widget.TextView

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun List<*>.deepEquals(other : List<*>) =
    this.size == other.size && this.mapIndexed { index, element -> element == other[index] }.all { it }


fun String.toFlagEmoji(): String {
    if (this.length != 2) {
        return this
    }
    val firstLetter = Character.codePointAt(this, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(this, 1) - 0x41 + 0x1F1E6

    // 2. It then checks if both characters are alphabet
    if (!this[0].isLetter() || !this[1].isLetter()) {
        return this
    }

    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}
