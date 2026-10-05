package com.getit.getit.yes.providers

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.getit.getit.yes.R
import com.getit.getit.yes.data.Provider

object ProviderActions {

    fun directions(context: Context, provider: Provider) {
        val uri = Uri.parse(
            "https://www.google.com/maps/dir/?api=1&destination=${provider.latitude},${provider.longitude}"
        )
        launch(context, Intent(Intent.ACTION_VIEW, uri))
    }

    fun call(context: Context, provider: Provider) {
        val phone = provider.phone.filter { it.isDigit() || it == '+' }
        if (phone.isEmpty() || phone.all { it == '0' }) {
            Toast.makeText(context, R.string.no_phone, Toast.LENGTH_SHORT).show()
            return
        }
        launch(context, Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)))
    }

    private fun launch(context: Context, intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.generic_error, Toast.LENGTH_SHORT).show()
        }
    }
}
