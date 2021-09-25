package com.bidbatl.dileveryapp.ui.login

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.bidbatl.dileveryapp.BaseActivity
import com.bidbatl.dileveryapp.R
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : BaseActivity() {
    private val LOCATION_AND_CAMERA = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button_sign_in).setOnClickListener {
            requestCameraAndLocationPermission()
        }
    }
    private fun hasLocationAndCameraPermissions(): Boolean {
        return EasyPermissions.hasPermissions(this, *LOCATION_AND_CAMERA)
    }

    @AfterPermissionGranted(Companion.RC_LOCATION_CAMERA_PERM)
    private fun requestCameraAndLocationPermission() {
        if (hasLocationAndCameraPermissions()) {
            // Have permissions, do the thing!
            moveToLoginActivity()
//            Toast.makeText(this, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show()
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                this,
                "Need Location and Camera permission",
                Companion.RC_LOCATION_CAMERA_PERM,
                *LOCATION_AND_CAMERA
            )
        }
    }


    private fun moveToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        this.startActivity(intent)
    }

    companion object {
        const val RC_LOCATION_CAMERA_PERM = 124
    }
}
