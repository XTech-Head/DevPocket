package com.xtech.xdevpocket.presentation.components

import android.content.Context
import android.content.Intent

/**
 * Wraps the system Sharesheet. Like clipboard, this is entirely user-initiated —
 * nothing is ever shared automatically, and x-DevPocket itself never transmits
 * this data anywhere on its own.
 */
object ShareHelper {

    fun share(context: Context, text: String, chooserTitle: String = "Share result") {
        if (text.isBlank()) return
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooser = Intent.createChooser(sendIntent, chooserTitle)
        context.startActivity(chooser)
    }
}
