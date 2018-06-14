package com.example.shivang.picturesque.PhotoEditor

import android.content.Context
import android.opengl.GLSurfaceView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.shivang.picturesque.Gallery.PhoneAlbum
import com.example.shivang.picturesque.R
import android.graphics.Bitmap
import android.opengl.GLException
import android.support.design.widget.BottomNavigationView
import android.util.Log
import java.nio.IntBuffer
import javax.microedition.khronos.opengles.GL10



class EffectsAdapter(private val mContext: Context,private val curUri : String,private val navigationView: BottomNavigationView) : RecyclerView.Adapter<EffectsAdapter.EffectsViewHolder>() {

    private var mEffectList: ArrayList<String>? = null
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    lateinit var view : View

    init {
        this.mEffectList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EffectsViewHolder {
        view = mInflater.inflate(R.layout.effect_list_card, parent, false)
        return EffectsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mEffectList?.size ?: 0
    }

    override fun onBindViewHolder(holder: EffectsViewHolder, position: Int) {
        val curEffect = mEffectList!![position]
        holder.effectName.text = curEffect
        holder.surfaceView.setEGLContextClientVersion(2)
        var er = EffectsRenderer(mContext,curUri)
        holder.surfaceView.setRenderer(er)
        holder.surfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        er.setCurEffect(position)
        holder.surfaceView.requestRender()
        Log.v("CurPic",er.bitmap.toString())
        holder.itemView.setOnClickListener({
            (mContext as PhotoEditor).onEffectClicked(position)
            navigationView.visibility = View.GONE
        })
    }

    fun setEffectList(effectList: List<String>) {
        this.mEffectList!!.clear()
        this.mEffectList!!.addAll(effectList)
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged()
    }


    inner class EffectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var surfaceView = itemView.findViewById(R.id.img_effect_preview) as GLSurfaceView
        var effectName = itemView.findViewById(R.id.effect_name) as TextView
    }




}