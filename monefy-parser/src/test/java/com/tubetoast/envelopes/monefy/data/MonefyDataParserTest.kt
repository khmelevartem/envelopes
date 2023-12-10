package com.tubetoast.envelopes.monefy.data

import org.junit.jupiter.api.Test
import java.io.File

class MonefyDataParserTest {

    private val parser = MonefyDataParser()

    @Test
    fun test() {
        val realFile = File("src/test/resources/Monefy.Data.10.12.2023.csv")
        parser.parse(realFile).forEach {
            println(it.category)
            it.transactions.forEach(::println)
            println()
        }
    }

    // TODO
}
