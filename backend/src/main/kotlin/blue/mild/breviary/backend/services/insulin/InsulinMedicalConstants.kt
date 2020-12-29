@file:Suppress("MagicNumber") // this is configuration map
// TODO please write some documentation to this file, where did we get these magic numbers

package blue.mild.breviary.backend.services.insulin

import blue.mild.breviary.backend.enums.InsulinType

const val CARBS_RATIO_NUMERATOR = 350
const val INSULIN_SENSIBILITY_NUMERATOR = 110

val INSULIN_EFFECT_IN_HOURS = mapOf(
    Pair(InsulinType.HUMALOG, 6),
    Pair(InsulinType.NOVORAPID, 6),
    Pair(InsulinType.APIDRA, 6),
    Pair(InsulinType.FIASP, 6),
    Pair(InsulinType.LYUMJEV, 5)
)

val EXPONENTIAL_ALMOST_ZERO_VALUE = mapOf(
    Pair(6, 1.8),
    Pair(5, 1.5)
)
