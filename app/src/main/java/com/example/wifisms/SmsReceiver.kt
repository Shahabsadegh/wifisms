package com.example.wifisms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.provider.Telephony
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {

    private val myNumber = "9128540857"

    override fun onReceive(ctx: Context, intent: Intent) {
        val msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent) ?: return
        for (m in msgs) {
            val from = m.originatingAddress ?: "?"
            val body = m.messageBody?.trim()?.lowercase() ?: ""
            Toast.makeText(ctx, "from=$from body=$body", Toast.LENGTH_LONG).show()

            if (!from.endsWith(myNumber)) {
                Toast.makeText(ctx, "شماره نخورد", Toast.LENGTH_LONG).show()
                continue
            }
            when (body) {
                "on" -> setWifi(ctx, true)
                "off" -> setWifi(ctx, false)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setWifi(ctx: Context, on: Boolean) {
        val wm = ctx.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ok = wm.setWifiEnabled(on)
        Toast.makeText(ctx, "wifi set=$on result=$ok", Toast.LENGTH_LONG).show()
    }
}
