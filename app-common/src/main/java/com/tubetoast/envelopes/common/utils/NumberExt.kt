package com.tubetoast.envelopes.common.utils

import com.tubetoast.envelopes.common.domain.models.Amount

fun Long.formatToReadableNumber(): String {
    val rev = toString().reversed()
    val strBuilder = StringBuilder()
    for (i in rev.indices) {
        if (i != 0 && i.mod(3) == 0 && rev.length >= i + 1) {
            strBuilder.append(" ")
        }
        strBuilder.append(rev[i])
    }
    return strBuilder.toString().reversed()
}

fun Amount.formatToReadableNumber(): String = units.formatToReadableNumber()
