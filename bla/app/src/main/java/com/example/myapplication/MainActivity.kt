package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.IOException
import android.util.Log
import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.google.android.things.contrib.driver.pwmservo.Servo

private val TAG = MainActivity::class.java.simpleName
private val PWM_BUS = "BUS NAME"

class MainActivity : AppCompatActivity() {
    private lateinit var mLocationManager: LocationManager
    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.v(TAG, "Location update: " + location)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }
    private lateinit var mServo: Servo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startLocationRequest()
        setupServo()
        try {
            mServo.setAngle(30.0)
        } catch (e: IOException) {
            Log.e(TAG, "Error setting the angle", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationRequest()
        destroyServo()
    }

    private fun startLocationRequest() {
        this.startService(Intent(this, GpsService::class.java))

        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // We need permission to get location updates
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // A problem occurred auto-granting the permission
            Log.d(TAG, "No permission")
            return
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0f, mLocationListener)
    }

    private fun stopLocationRequest() {
        this.stopService(Intent(this, GpsService::class.java))
        mLocationManager.removeUpdates(mLocationListener)
    }

    private fun setupServo() {
        try {
            mServo = Servo(PWM_BUS)
            mServo.setAngleRange(0.0, 180.0)
            mServo.setEnabled(true)
        } catch (e: IOException) {
            Log.e(TAG, "Error creating Servo", e)
        }

    }

    private fun destroyServo() {
        try {
            mServo.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing Servo")
        }
    }

}
