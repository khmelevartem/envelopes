package com.ncloudtech.cloudoffice.benchmark

import android.content.Context
import android.os.Environment
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files


fun useAssetFile(
    assetFilePath: String,
    tempFolder: File? = null,
    outputFilePath: String = assetFilePath
): File = copyTestAssetIntoTempFolder(
    assetFilePath = assetFilePath,
    tempFolder = tempFolder,
    outputFilePath = outputFilePath
)

private val testContext: Context get() = InstrumentationRegistry.getInstrumentation().context
private val targetContext: Context get() = InstrumentationRegistry.getInstrumentation().targetContext

private fun copyTestAssetIntoTempFolder(
    assetFilePath: String,
    tempFolder: File? = null,
    outputFilePath: String = assetFilePath
): File {
    val outFile = File(
        tempFolder ?: File(getCacheDir(targetContext), "temp"),
        outputFilePath
    )

    Files.createDirectories(outFile.parentFile?.toPath())
    outFile.delete()

    testContext.assets.open(assetFilePath).use { input ->
        FileOutputStream(outFile).use { output ->
            input.copyTo(output, 1024)
        }
    }

    return outFile
}

private var cacheDir: File? = null

private fun getCacheDir(context: Context): File {
    if (folderExists(cacheDir?.absolutePath)) {
        return cacheDir!!
    }

    val externalStorageAvailable =
        Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    cacheDir = context.filesDir
    if (externalStorageAvailable) {
        val externalFilesDir= context.getExternalFilesDir(null)
        if (externalFilesDir != null) {
            cacheDir = externalFilesDir
        }
    } else {
        cacheDir = context.cacheDir
    }

    return cacheDir!!
}

private fun folderExists(path: String?): Boolean {
    return path?.let { File(it).run { exists() && isDirectory } } ?: false
}
