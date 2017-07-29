package ca.nburaktaban.amispaying

import android.app.*
import android.content.Intent
import android.hardware.Camera
import android.os.IBinder
import android.util.Log
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.NotificationCompat


class SpyService : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()


        val t = object : Thread() {
            override fun run() {
                try {
                    while (1 < 2) {
                        var delay = 2500L
                        Runnable {
                            if (isCameraUsebyApp() && ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                sendNotification()
                                delay = 10000L
                            }
                        }.run()
                        Thread.sleep(delay)
                    }
                } finally {

                }
            }
        }
        t.start()
    }

    private fun sendNotification() {
        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_camera_alt_black_24dp)
                .setContentTitle("You Are Spying!")
                .setContentText("Someones may be watching you!")
        val resultIntent = Intent(this, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder.setContentIntent(resultPendingIntent)
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(0, mBuilder.build())

    }

    fun isCameraUsebyApp(): Boolean {
        var camera: Camera? = null
        try {
            camera = Camera.open()
        } catch (e: RuntimeException) {
            return true
        } finally {
            if (camera != null) camera!!.release()
        }
        return false
    }
}
