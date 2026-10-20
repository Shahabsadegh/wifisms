package com.example.wifisms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.provider.Telephony
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {

    private val myNumber = "9128540857"

    override fun onReceive(ctx: Context, intent: Intent) {
        val msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent) ?: return
        for (m in msgs) {
            val from = m.originatingAddress ?: continue
            if (!from.endsWith(myNumber)) continue
            when (m.messageBody?.trim()?.lowercase()) {
                "on" -> setWifi(ctx, true)
                "off" -> setWifi(ctx, false)
                "hon" -> setHotspot(ctx, true)
                "hoff" -> setHotspot(ctx, false)
            }
        }
    }

    private fun toast(ctx: Context, t: String) =
        Toast.makeText(ctx, t, Toast.LENGTH_LONG).show()

    @Suppress("DEPRECATION")
    private fun setWifi(ctx: Context, on: Boolean) {
        val wm = ctx.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ok = wm.setWifiEnabled(on)
        toast(ctx, "wifi=$on result=$ok")
    }

    private fun setHotspot(ctx: Context, on: Boolean) {
        try {
            val wm = ctx.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val m = wm.javaClass.getMethod(
                "setWifiApEnabled",
                WifiConfiguration::class.java,
                Boolean::class.javaPrimitiveType
            )
            val ok = m.invoke(wm, null, on)
            toast(ctx, "hotspot=$on result=$ok")
        } catch (e: Exception) {
            toast(ctx, "hotspot error: ${e.cause ?: e}")
        }
    }
}
