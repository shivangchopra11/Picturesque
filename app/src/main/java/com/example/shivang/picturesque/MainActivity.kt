package com.example.shivang.picturesque

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
import android.support.v4.app.FragmentActivity


class MainActivity : FragmentActivity(), OnRequestPermissionsResultCallback,PermissionResultCallback {

    var permissions: ArrayList<String> = ArrayList()
    var permissionUtils: PermissionUtils? = null

    private val TAG : String = "TAG"
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
//            R.id.navigation_home -> {
//                message.setText(R.string.title_home)
//                return@OnNavigationItemSelectedListener true
//            }
            R.id.navigation_camera -> {


                return@OnNavigationItemSelectedListener true
            }
//            R.id.navigation_notifications -> {
//                message.setText(R.string.title_notifications)
//                return@OnNavigationItemSelectedListener true
//            }
        }
        false
    }

    private fun showCameraPreview() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainFrag, CameraFragment.newInstance() as Fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionUtils =  PermissionUtils(this)

        permissions.add(Manifest.permission.CAMERA)
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)

        permissionUtils!!.checkPermission(permissions,"The app needs external storage and camera permissions",1)
//        showCameraPreview()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    override fun PermissionGranted(request_code: Int) {

        showCameraPreview()
        Log.i("PERMISSION","GRANTED")
    }

    override fun PartialPermissionGranted(request_code: Int, granted_permissions: ArrayList<String>) {


        Log.i("PERMISSION PARTIALLY","GRANTED")
    }

    override fun PermissionDenied(request_code: Int) {

        permissionUtils!!.checkPermission(permissions,"The app needs external storage and camera permissions",1)
        Log.i("PERMISSION","DENIED")
    }

    override fun NeverAskAgain(request_code: Int) {

        Log.i("PERMISSION","NEVER ASK AGAIN");
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionUtils!!.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }
}
