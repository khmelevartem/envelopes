package com.tubetoast.envelopes.common.util

import com.tubetoast.envelopes.common.domain.models.Date

interface DateGenerator {
    fun now(): Date
}