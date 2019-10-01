package com.example.managedata.utility

import android.app.Application
import android.content.Context
import java.io.File
import java.nio.charset.Charset

class FileHelper {
    companion object {
        fun getTextFromResource(context: Context, resourceId: Int): String {
            return context.resources.openRawResource(resourceId).use {
                it.bufferedReader().use {
                    it.readText()
                }
            }
        }

        fun getTextFromAssets(context: Context, fileName: String): String {
            return context.assets.open(fileName).use {
                it.bufferedReader().use {
                    it.readText()
                }
            }
        }

        fun saveTextToFile(app: Application, json: String?) {
            val file = File(app.filesDir, "monsters.json")
            file.writeText(json ?: "", Charsets.UTF_8)
        }

        fun readTextFile(app: Application): String? {
            val file = File(app.filesDir, "monsters.json")
            return if (file.exists()) {
                file.readText()
            } else {
                null
            }
        }

    }
}