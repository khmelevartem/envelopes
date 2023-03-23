package com.tubetoast.envelopes.android.util

import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.util.DateGenerator
import java.util.Calendar

class JavaDateGenerator : DateGenerator {
    override fun now(): Date =
        Calendar.getInstance().run {
            Date(
                minute = get(Calendar.MINUTE),
                hour = get(Calendar.HOUR_OF_DAY),
                day = get(Calendar.DAY_OF_MONTH),
                month = get(Calendar.MONTH),
                year = get(Calendar.YEAR),
            )
        }
}