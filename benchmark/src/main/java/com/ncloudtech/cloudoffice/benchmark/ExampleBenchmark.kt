/*
 * Copyright (c) New Cloud Technologies, Ltd., 2013-2024
 *
 * You can not use the contents of the file in any way without New Cloud Technologies, Ltd. written permission.
 * To obtain such a permit, you should contact New Cloud Technologies, Ltd. at https://myoffice.ru/contacts/
 *
 */

package com.ncloudtech.cloudoffice.benchmark

import android.net.Uri
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    private val packageName = "com.tubetoast.envelopes"

    @Test
    fun launchApp() = startApp()

    @Test
    fun launchAppWithFull() = startApp("data.csv")

    @Test
    fun launchAppWithHalf() = startApp("data.2.csv")

    @Test
    fun launchAppWithQuarter() = startApp("data.4.csv")

    @Test
    fun launchAppWithEights() = startApp("data.8.csv")

    private fun startApp(uri: String? = null) = benchmarkRule.measureRepeated(
        packageName = packageName,
        metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        setupBlock = MacrobenchmarkScope::pressHome
    ) {
        uri?.let {
            startActivityAndWait {
                it.data = Uri.fromFile(useAssetFile(uri))
            }
        } ?: startActivityAndWait()

    }

}
