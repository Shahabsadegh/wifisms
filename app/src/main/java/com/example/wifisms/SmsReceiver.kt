package com.example.wifisms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.provider.Telephony

class SmsReceiver : BroadcastReceiver() {

    // شماره‌ی خودت: ۱۰ رقم آخر، بدون صفر اول و بدون +98
    private val myNumber = "9128540857"

    override fun onReceive(ctx: Context, intent: Intent) {
        val msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent) ?: return
        for (m in msgs) {
            val from = m.originatingAddress ?: continue
            if (!from.endsWith(myNumber)) continue
            when (m.messageBody?.trim()?.lowercase()) {
                "on" -> setWifi(ctx, true)
                "off" -> setWifi(ctx, false)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setWifi(ctx: Context, on: Boolean) {
        val wm = ctx.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wm.isWifiEnabled = on
    }
}
