package blue.mild.breviary.backend.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

/**
 * String to int.
 *
 * @param shortAsText
 * @return
 */
fun parseStringToInt(shortAsText: String): Int {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.parse(shortAsText).toInt()
}

/**
 * Stirng to double.
 *
 * @param string
 * @return
 */
fun parseStringToDouble(string: String): Double {
    val decimalFormat = NumberFormat.getInstance()
    return decimalFormat.parse(string).toDouble()
}

/**
 * German string to int.
 *
 * @param shortAsText
 * @return
 */
fun parseGermanStringToInt(shortAsText: String): Int {
    val decimalFormat = DecimalFormat("#,###", DecimalFormatSymbols.getInstance(Locale.GERMAN))
    return decimalFormat.parse(shortAsText).toInt()
}

/**
 * German string to double.
 *
 * @param string
 * @return
 */
fun parseGermanStringToDouble(string: String): Double {
    val decimalFormat = NumberFormat.getInstance(Locale.GERMAN)
    return decimalFormat.parse(string).toDouble()
}
