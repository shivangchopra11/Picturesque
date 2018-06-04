package com.example.shivang.picturesque.Camera

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.example.shivang.picturesque.PhotoEditor.PhotoEditor
import com.example.shivang.picturesque.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CameraPreview : AppCompatActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_preview)
        var imageView : ImageView = findViewById(R.id.img_prev)
        imageView.setImageBitmap(CameraFragment.bitmap)
        val btn_ok : ImageButton = findViewById(R.id.btn_ok_prev)
        val btn_clear : ImageButton = findViewById(R.id.btn_clear_prev)
        val btn_edit : ImageButton = findViewById(R.id.btn_edit_prev)
        btn_ok.setOnClickListener(this)
        btn_clear.setOnClickListener(this)
        btn_edit.setOnClickListener(this)

    }

    private fun createImageFile(galleryFolder : File) : File {
        val timeStamp : String = object : SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()){}.format(object : Date(){})
        val imageFileName : String = "image_" + timeStamp + "_"
        return File.createTempFile(imageFileName, ".jpg", galleryFolder)
    }

    override fun onClick(v: View?) {
        if(v!!.id==R.id.btn_clear_prev) {
            super.onBackPressed()
        }
        else if(v!!.id==R.id.btn_ok_prev) {
            AsyncTask.execute({
                var curFile = createImageFile(CameraFragment.galleryFolder!!)
                val outputPhoto = FileOutputStream(curFile)
                CameraFragment.bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputPhoto)
                outputPhoto.close()
            })
            super.onBackPressed()
        }
        else {
            AsyncTask.execute({
                var curFile = createImageFile(CameraFragment.galleryFolder!!)
                val outputPhoto = FileOutputStream(curFile)
                CameraFragment.bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputPhoto)
                outputPhoto.close()
                var curUri = Uri.fromFile(curFile)
                Log.v("URIPREV",curUri.toString())
                var intent = Intent(this,PhotoEditor::class.java)
                intent.putExtra("curPic",curUri.toString())
                startActivity(intent)
            })
        }
    }
}
