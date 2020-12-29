@file:Suppress("MagicNumber") // this is configuration map
// TODO please write some documentation to this file, where did we get these magic numbers

package blue.mild.breviary.backend.services.heparin


const val LOWEST_APTT = 1.2f
const val LOW_APTT = 1.5f
const val STANDARD_APTT = 2.3f
const val HIGH_APTT = 3f
const val HIGHEST_APTT = 3.5f

const val LOWEST_APTT_DOSAGE_PER_KG_CHANGE = 4f
const val LOW_APTT_DOSAGE_PER_KG_CHANGE = 2f
const val BELOW_STANDARD_APTT_DOSAGE_PER_KG_CHANGE = 1f
const val ABOVE_STANDARD_APTT_DOSAGE_PER_KG_CHANGE = -1f
const val HIGH_APTT_DOSAGE_PER_KG_CHANGE = -2f
const val HIGHEST_APTT_DOSAGE_PER_KG_CHANGE = -3f

const val LOWEST_APTT_BOLUS = 80f
const val LOW_APTT_BOLUS = 40f

const val REMAINDER_STANDARD_HOURS = 6
const val REMAINDER_FIRST_HOURS = 4
const val REMINDER_NON_COAGULATING_HOURS = 1

const val EXTREME_DOSAGE_DIFF = 15f

const val MIN_WEIGHT_KG = 50f
const val MAX_WEIGHT_KG = 100f

const val DEFAULT_UNITS_PER_KG = 18f
