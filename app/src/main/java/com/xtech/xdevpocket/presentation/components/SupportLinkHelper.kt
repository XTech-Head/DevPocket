package com.xtech.xdevpocket.presentation.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * Opens x-DevPocket's "Buy Me a Coffee" page in the user's browser.
 *
 * Like [ShareHelper], this is entirely user-initiated — tapping the button is
 * the only thing that triggers it, and the app itself makes no network call;
 * it just hands off to whatever browser/app the device already has installed
 * for https links. This is the one link in the app that leaves the device,
 * and it only fires when the person deliberately taps it.
 */
object SupportLinkHelper {

    // TODO: replace with Sammy's real Buy Me a Coffee page before shipping.
    const val BUY_ME_A_COFFEE_URL = "https://buymeacoffee.com/xammyhuncho"

    fun openBuyMeACoffee(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BUY_ME_A_COFFEE_URL))
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No browser app found to open the link.", Toast.LENGTH_SHORT).show()
        }
    }
}
