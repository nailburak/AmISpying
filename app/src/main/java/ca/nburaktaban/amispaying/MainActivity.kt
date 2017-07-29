package ca.nburaktaban.amispaying

import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val PERMISSION_REQUEST = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, Array(1, {android.Manifest.permission.CAMERA}), PERMISSION_REQUEST)
        else {
            load()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if( requestCode == PERMISSION_REQUEST){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                load()
            else
                Snackbar.make(main, "You need give a permission to use this feature", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun load() {
        main.postDelayed({
            startService(Intent(this, SpyService::class.java))
        }, 2500)

        check_btn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, Array(1, {android.Manifest.permission.CAMERA}), PERMISSION_REQUEST)
            else
                check.text = if (isCameraUsebyApp()) "You Are Spying!" else "You Are Not Spying"
        }
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
