package com.tubetoast.envelopes.monefy.data

import java.io.InputStream

class MonefySource(
    input: InputStream,
    monefyDataParser: MonefyDataParser = MonefyDataParser()
) {

    val categorySnapshots = input.reader().use { monefyDataParser.parse(it.readLines()).toSet() }
}
