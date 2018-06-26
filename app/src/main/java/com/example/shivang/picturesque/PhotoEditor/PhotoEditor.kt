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
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import com.example.shivang.picturesque.Gallery.AlbumAdapter
import com.example.shivang.picturesque.Gallery.SpacesItemDecoration
import kotlinx.android.synthetic.main.activity_photo_editor.*


class PhotoEditor : AppCompatActivity() {

    lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: EffectsAdapter

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.effect_filter -> {
                navigationView.menu.getItem(1).isChecked = true
                effect_transform_bar.visibility = View.GONE
                mRecyclerView.adapter = mAdapter
                mAdapter.setEffectList(EffectNames.filters,0)
            }
            R.id.effect_enhance -> {
                navigationView.menu.getItem(2).isChecked = true
                effect_transform_bar.visibility = View.GONE
                mRecyclerView.adapter = mAdapter
                mAdapter.setEffectList(EffectNames.enhance,1)
            }
            R.id.effect_transform -> {
                navigationView.menu.getItem(0).isChecked = true
                effect_transform_bar.visibility = View.VISIBLE

            }
        }
        false
    }

    lateinit var surfaceView : GLSurfaceView
    lateinit var curUri : String
    lateinit var er : EffectsRenderer
    lateinit var navigationView: BottomNavigationView
    lateinit var effectDoneBtn : ImageButton
    lateinit var seekBar : SeekBar
    private var curPos : Int = 0
    var mFactor : Float = 0.0f

    var seekBarChangeListener: SeekBar.OnSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            // updated continuously as the user slides the thumb
            er.setCurEffect(curPos)
//            Log.v("EffectPos",curPos.toString())
            mFactor = progress / (100.0f)
            er.setFactor(mFactor)
            surfaceView.requestRender()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            // called when the user first touches the SeekBar
            er.setCurEffect(curPos)
            er.setFactor(mFactor)
            surfaceView.requestRender()
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            // called after the user finishes moving the SeekBar
            er.setCurEffect(curPos)
            er.setFactor(mFactor)
            surfaceView.requestRender()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_editor)
        surfaceView = findViewById(R.id.photo_for_edit)
        surfaceView.setEGLContextClientVersion(2)
        navigationView = findViewById(R.id.navigationEffect)
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        effectDoneBtn = findViewById(R.id.effect_done_btn)
        seekBar = findViewById(R.id.seek_bar_effect)
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        effectDoneBtn.setOnClickListener{
            navigationView.visibility = View.VISIBLE
        }

        curUri = intent.getStringExtra("curPic")
        er = EffectsRenderer(this,curUri)
        surfaceView.setRenderer(er)
        surfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        mRecyclerView = findViewById(R.id.effects_recycler_view)
        mRecyclerView.addItemDecoration(SpacesItemDecoration(4))
        mRecyclerView.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)

        mAdapter = EffectsAdapter(this,curUri,navigationView)
        mRecyclerView.adapter = mAdapter
//        mAdapter.setEffectList(EffectNames.filters,0)


        effect_rotate.setOnClickListener{
            onEffectClicked(20)
            navigationView.visibility = View.GONE
        }

    }

    fun onEffectClicked(pos : Int) {
//        surfaceView.setRenderer(EffectsRenderer(this,curUri,pos))
        curPos = pos
        er.setCurEffect(pos)
        surfaceView.requestRender()
    }


}
