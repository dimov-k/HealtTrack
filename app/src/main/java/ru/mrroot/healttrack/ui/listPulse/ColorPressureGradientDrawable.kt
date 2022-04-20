package ru.mrroot.healttrack.ui.listPulse

import android.graphics.drawable.GradientDrawable

class ColorPressureGradientDrawable {

    var white = 0XFFFFFFFF.toInt()
    var read = 0X50C51C1C.toInt()
    var yellow = 0X50DCC609.toInt()
    var green1 = 0X50167501.toInt()
    var green2 = 0X5078F808.toInt()

    var listColor = intArrayOf(
        white,
        white,
        white,
        white,
        white,
        white
    )

    fun getGradient(upperPressure : Int, lowerPressure: Int): GradientDrawable {
        listColor[2] = getColorUpperPressure(upperPressure)
        listColor[3] = getColorLowerPressure(lowerPressure)
        return GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            listColor
        )
    }

    fun getColorUpperPressure(pressure: Int): Int {
        if (pressure >= 140) return read
        if (pressure in 130..139) return yellow
        if (pressure in 120..129) return green1
        if (120 > pressure) return green2
        return white
    }

    fun getColorLowerPressure(pressure: Int): Int {
        if (pressure >= 100) return read
        if (pressure in 85..99) return yellow
        if (pressure in 80..84) return green1
        if (80 > pressure) return green2
        return white
    }

}