package com.example.wifisms

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(Manifest.permission.RECEIVE_SMS), 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!Settings.System.canWrite(this)) {
            val i = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            i.data = Uri.parse("package:$packageName")
            startActivity(i)
        }
        finish()
    }
}
