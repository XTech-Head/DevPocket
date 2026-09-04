package com.xtech.xdevpocket.presentation.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Thin wrapper around the system clipboard. Nothing here ever touches the network —
 * copy/paste stays entirely on-device, which matters for the sensitive data
 * (tokens, keys, source snippets) this app is designed to handle.
 */
object ClipboardHelper {

    fun copyToClipboard(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard?.setPrimaryClip(clip)
    }

    fun pasteFromClipboard(context: Context): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = clipboard?.primaryClip
        if (clip != null && clip.itemCount > 0) {
            return clip.getItemAt(0).coerceToText(context)?.toString()
        }
        return null
    }
}
