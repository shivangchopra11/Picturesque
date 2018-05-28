package com.example.shivang.picturesque.Permissions

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.Toast


class PermissionUtils(val context: Context) {

//    var current_activity: Activity? = context as Activity

    var permissionResultCallback: PermissionResultCallback? =  context as PermissionResultCallback
    var permission_list: ArrayList<String> = ArrayList()
    var listPermissionsNeeded: ArrayList<String> = ArrayList()
    var dialog_content = ""
    var req_code: Int = 0



    fun checkPermission(permissions: ArrayList<String>, dialog_content: String, request_code: Int) {
        this.permission_list = permissions
        this.dialog_content = dialog_content
        this.req_code = request_code

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions(permissions, request_code)) {
                permissionResultCallback!!.PermissionGranted(request_code)
                Log.i("all permissions", "granted")
                Log.i("proceed", "to callback")
            }
        } else {
            permissionResultCallback!!.PermissionGranted(request_code)

            Log.i("all permissions", "granted")
            Log.i("proceed", "to callback")
        }

    }

    fun checkAndRequestPermissions(permissions: ArrayList<String>, request_code: Int): Boolean {

        if (permissions.size > 0) {
            listPermissionsNeeded = ArrayList()

            for (i in 0 until permissions.size) {
                val hasPermission = ContextCompat.checkSelfPermission(context, permissions[i])

                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permissions[i])
                }

            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(context as Activity, listPermissionsNeeded.toArray(arrayOfNulls(listPermissionsNeeded.size)), request_code)
                return false
            }
        }

        return true
    }

    fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(context as Activity)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show()
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> if (grantResults.isNotEmpty()) {
                val perms = HashMap<String,Int>()

                for (i in permissions.indices) {
                    perms[permissions[i]] = grantResults[i]
                }

                val pendingPermissions = ArrayList<String>()

                for (i in 0 until listPermissionsNeeded.size) {
                    if (perms[listPermissionsNeeded[i]] != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, listPermissionsNeeded[i]))
                            pendingPermissions.add(listPermissionsNeeded[i])
                        else {
                            Log.i("Go to settings", "and enable permissions")
                            permissionResultCallback!!.NeverAskAgain(req_code)
                            Toast.makeText(context, "Go to settings and enable permissions", Toast.LENGTH_LONG).show()
                            return
                        }
                    }

                }

                if (pendingPermissions.size > 0) {
                    showMessageOKCancel(dialog_content,
                            DialogInterface.OnClickListener { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> checkPermission(permission_list, dialog_content, req_code)
                                    DialogInterface.BUTTON_NEGATIVE -> {
                                        Log.i("permisson", "not fully given")
                                        if (permission_list.size === pendingPermissions.size)
                                            permissionResultCallback!!.PermissionDenied(req_code)
                                        else
                                            permissionResultCallback!!.PartialPermissionGranted(req_code, pendingPermissions)
                                    }
                                }
                            })

                } else {
                    Log.i("all", "permissions granted")
                    Log.i("proceed", "to next step")
                    permissionResultCallback!!.PermissionGranted(req_code)

                }


            }
        }
    }
}