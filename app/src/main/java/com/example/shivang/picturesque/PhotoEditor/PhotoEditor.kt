package com.example.shivang.picturesque.PhotoEditor

import android.content.Context
import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.shivang.picturesque.R
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.BitmapFactory
import android.graphics.Bitmap



class PhotoEditor : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_editor)
        var surfaceView : GLSurfaceView = findViewById(R.id.photo_for_edit)
        surfaceView.setEGLContextClientVersion(2)

        var curUri : String = intent.getStringExtra("curPic")
        surfaceView.setRenderer(EffectsRenderer(this,curUri))
        surfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

    }


}
