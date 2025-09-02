package com.example.filllight

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout

class OverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var lightView: LinearLayout

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        lightView = LinearLayout(this).apply {
            setBackgroundColor(Color.WHITE)
            alpha = 0.8f // default intensity
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.CENTER
        windowManager.addView(lightView, params)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::lightView.isInitialized) windowManager.removeView(lightView)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
