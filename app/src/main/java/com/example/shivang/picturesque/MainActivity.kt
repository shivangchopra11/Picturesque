package com.example.shivang.picturesque

import android.Manifest
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.Fragment
import android.util.Log
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.example.shivang.picturesque.Camera.CameraFragment
import com.example.shivang.picturesque.Gallery.GalleryFragment
import com.example.shivang.picturesque.Permissions.PermissionResultCallback
import com.example.shivang.picturesque.Permissions.PermissionUtils


class MainActivity : AppCompatActivity(), OnRequestPermissionsResultCallback, PermissionResultCallback {

    var permissions: ArrayList<String> = ArrayList()
    var permissionUtils: PermissionUtils? = null

    private val TAG : String = "TAG"
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_gallery -> {
                showGallery()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                showCameraPreview();
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun showCameraPreview() {
        supportActionBar!!.hide()
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainFrag, CameraFragment.newInstance() as Fragment)
                .commit()
    }

    private fun showGallery() {
        supportActionBar!!.show()
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainFrag, GalleryFragment.newInstance() as Fragment)
                .commit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)



        permissionUtils = PermissionUtils(this)

        permissions.add(Manifest.permission.CAMERA)
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)

        permissionUtils!!.checkPermission(permissions,"The app needs external storage and camera permissions",1)
//        showCameraPreview()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.menu.getItem(2).isChecked = true
        showGallery()
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

    override fun onBackPressed() {
        if(navigation.menu.getItem(2).isChecked) {
            if(intent.getIntExtra("status",0)==1) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrag, GalleryFragment.newInstance() as Fragment)
                        .commit()
                setActionBarTitle(resources.getString(R.string.app_name))
                intent.putExtra("status",0)

            }
            else {
                super.onBackPressed()
            }
        }
        else
            super.onBackPressed()
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }
}
