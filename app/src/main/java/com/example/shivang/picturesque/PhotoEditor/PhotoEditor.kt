package com.example.shivang.picturesque.PhotoEditor

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.shivang.picturesque.R
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.shivang.picturesque.Gallery.AlbumAdapter
import com.example.shivang.picturesque.Gallery.SpacesItemDecoration


class PhotoEditor : AppCompatActivity() {

    lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: EffectsAdapter

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.effect_filter -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.effect_enhance -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.effect_transform -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    lateinit var surfaceView : GLSurfaceView
    lateinit var curUri : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_editor)
        surfaceView = findViewById(R.id.photo_for_edit)
        surfaceView.setEGLContextClientVersion(2)

        curUri = intent.getStringExtra("curPic")
        surfaceView.setRenderer(EffectsRenderer(this,curUri))
        surfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        mRecyclerView = findViewById(R.id.effects_recycler_view)
        mRecyclerView.addItemDecoration(SpacesItemDecoration(4))
        mRecyclerView.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)

        mAdapter = EffectsAdapter(this)
        mRecyclerView.adapter = mAdapter
        mAdapter.setEffectList(EffectNames.filters)

    }

    fun onEffectClicked(pos : Int) {
//        surfaceView.setRenderer(EffectsRenderer(this,curUri,pos))
        EffectsRenderer.setCurEffect(pos)
        surfaceView.requestRender()
    }


}
