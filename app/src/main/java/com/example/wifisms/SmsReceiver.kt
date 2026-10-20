package com.example.wifisms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.provider.Telephony
import android.widget.Toast
import com.android.dx.stock.ProxyBuilder
import java.lang.reflect.InvocationHandler

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

    @Suppress("UNCHECKED_CAST")
    private fun setHotspot(ctx: Context, on: Boolean) {
        try {
            val cm = ctx.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (on) {
                val cbClass = Class.forName(
                    "android.net.ConnectivityManager\$OnStartTetheringCallback"
                ) as Class<Any>
                val handler = InvocationHandler { proxy, method, args ->
                    ProxyBuilder.callSuper(proxy, method, *(args ?: emptyArray()))
                }
                val cb = ProxyBuilder.forClass(cbClass)
                    .dexCache(ctx.codeCacheDir)
                    .handler(handler)
                    .build()
                val m = ConnectivityManager::class.java.getMethod(
                    "startTethering",
                    Int::class.javaPrimitiveType,
                    Boolean::class.javaPrimitiveType,
                    cbClass
                )
                m.invoke(cm, 0, false, cb)
                toast(ctx, "hotspot start sent")
            } else {
                val m = ConnectivityManager::class.java.getMethod(
                    "stopTethering", Int::class.javaPrimitiveType
                )
                m.invoke(cm, 0)
                toast(ctx, "hotspot stop sent")
            }
        } catch (e: Throwable) {
            toast(ctx, "hotspot error: ${e.cause ?: e}")
        }
    }
}
