package com.tubetoast.envelopes.monefy

import org.junit.jupiter.api.Test
import java.io.File

class MonefyDataParserTest {

    private val parser = MonefyDataParser(
        File("src/test/resources/Monefy.Data.10.12.2023.csv"),
    )

    @Test
    fun test() {
        parser.parse().forEach(::println)
    }
}
